package cursola.view

import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun <Sample> Chart(
    samples: List<Sample>,
    pathCalculator: PathCalculator<Sample>,
    background: Brush,
    modifier: Modifier = Modifier
) {
    var path by remember { mutableStateOf(Path()) }
    var size by remember { mutableStateOf(Rect.Zero) }
    val rect by animateRectAsState(size, animationSpec = remember { tween() })

    LaunchedEffect(samples, rect) {
        path = pathCalculator.getPath(samples, rect)
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                size = Rect(
                    offset = Offset.Zero,
                    size = Size(
                        width = it.width.toFloat(),
                        height = it.height.toFloat()
                    )
                )
            }
            .drawWithContent {
                drawPath(path = path, brush = background)
            }
    )
}

@Composable
fun ChartDouble(
    samples: List<Double>,
    background: Brush,
    modifier: Modifier = Modifier
) {
    val factory = remember {
        val result: CoordinateCalculatorFactory<Double>
        result = CoordinateCalculatorFactoryDouble()
        result
    }
    val calculator = remember {
        var result: PathCalculator<Double>
        result = PathCalculatorDouble(factory)
        result = PathCalculatorSampleCountSwitch(
            sourceAbove = result,
            sourceBelow = PathCalculatorRectangle(),
            sourceEqual = PathCalculatorRectangle(),
            sampleCount = 2
        )
        result
    }

    Chart(
        modifier = modifier,
        samples = samples,
        pathCalculator = calculator,
        background = background
    )
}

interface PathCalculator<Sample> {

    suspend fun getPath(samples: List<Sample>, rect: Rect): Path

}

class PathCalculatorDouble(
    private val factory: CoordinateCalculatorFactory<Double>
) : PathCalculator<Double> {

    override suspend fun getPath(
        samples: List<Double>,
        rect: Rect
    ): Path = withContext(Dispatchers.IO) {
        val metadata = ChartMetadata(
            container = rect,
            minY = samples.minOrNull() ?: 0.0,
            maxY = samples.maxOrNull() ?: 0.0,
            count = samples.size
        )
        val coordinates = factory.build(metadata)
        val path = Path()

        for ((index, sample) in samples.withIndex()) {
            val x = coordinates.getX(sample, index)
            val y = coordinates.getY(sample, index)
            if (index == 0) {
                path.moveTo(x, y)
                continue
            }

            val controlX = x - coordinates.strideX / 2f
            val lastIndex = index - 1
            val lastY = coordinates.getY(samples[lastIndex], lastIndex)
            path.cubicTo(
                controlX, lastY,
                controlX, y,
                x, y
            )
        }
        path.lineTo(rect.right, coordinates.getY(samples.last(), samples.size - 1))

        // finish the line off to make a "chart" shape
        path.lineTo(rect.right, rect.bottom)
        path.lineTo(0f, rect.bottom)
        path.close()

        return@withContext path
    }

}


class CoordinateCalculatorFactoryDouble : CoordinateCalculatorFactory<Double> {

    override fun build(metadata: ChartMetadata<Double>): CoordinateCalculator<Double> {
        return CoordinateCalculatorDouble(metadata)
    }
}


interface CoordinateCalculatorFactory<Sample> {

    fun build(metadata: ChartMetadata<Sample>): CoordinateCalculator<Sample>

}


interface CoordinateCalculator<Sample> {

    val strideX: Float

    fun getX(sample: Sample, index: Int): Float
    fun getY(sample: Sample, index: Int): Float

}

class CoordinateCalculatorDouble(
    private val metadata: ChartMetadata<Double>
) : CoordinateCalculator<Double> {

    override val strideX: Float = metadata.container.width / (metadata.count - 1)

    override fun getX(sample: Double, index: Int): Float {
        return index * strideX + metadata.container.left
    }

    override fun getY(sample: Double, index: Int): Float {
        val min = metadata.minY
        val max = metadata.maxY
        val heightPercentage = (1 - ((sample - min) / (max - min))).toFloat()
        return metadata.container.height * heightPercentage
    }

}

data class ChartMetadata<Sample>(
    val container: Rect,
    val minY: Sample,
    val maxY: Sample,
    val count: Int
)

class PathCalculatorSampleCountSwitch<Sample>(
    private val sourceBelow: PathCalculator<Sample>,
    private val sourceAbove: PathCalculator<Sample>,
    private val sourceEqual: PathCalculator<Sample>,
    private val sampleCount: Int
) : PathCalculator<Sample> {

    override suspend fun getPath(samples: List<Sample>, rect: Rect): Path {
        return when (samples.size) {
            in Int.MIN_VALUE until sampleCount -> sourceBelow.getPath(samples, rect)
            sampleCount -> sourceEqual.getPath(samples, rect)
            else -> sourceAbove.getPath(samples, rect)
        }
    }

}

class PathCalculatorRectangle<Sample> : PathCalculator<Sample> {

    override suspend fun getPath(samples: List<Sample>, rect: Rect): Path {
        return Path().apply {
            moveTo(rect.left, rect.center.y)
            lineTo(rect.right, rect.center.y)
            lineTo(rect.right, rect.bottom)
            lineTo(0f, rect.bottom)
            close()
        }
    }

}