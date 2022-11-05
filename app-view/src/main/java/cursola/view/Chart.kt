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
import cursola.view.chart.HorizontalPointResolver
import cursola.view.chart.VerticalPointResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun <Sample> Chart(
    samples: List<Sample>,
    pathCalculator: PathCalculator<Sample>,
    background: Brush,
    initialSize: Rect,
    modifier: Modifier = Modifier
) {
    var path by remember { mutableStateOf(Path()) }
    var size by remember { mutableStateOf(initialSize) }
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
    initialSize: Rect,
    modifier: Modifier = Modifier
) {
    val calculator = remember {
        var result: PathCalculator<Double>
        result = PathCalculatorDouble()
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
        background = background,
        initialSize = initialSize
    )
}

interface PathCalculator<Sample> {

    suspend fun getPath(samples: List<Sample>, rect: Rect): Path

}

class PathCalculatorDouble : PathCalculator<Double> {

    override suspend fun getPath(
        samples: List<Double>,
        rect: Rect
    ): Path = withContext(Dispatchers.IO) {
        val stride = rect.width / (samples.size - 1)
        val xPoints = HorizontalPointResolver(stride, offset = rect.left)
        val (min, max) = samples.minMax() ?: (0.0 to 0.0)
        val yPoints = VerticalPointResolver(min, max, rect.height)
        val path = Path()

        for ((index, sample) in samples.withIndex()) {
            val x = xPoints.get(index)
            val y = yPoints.get(sample)
            if (index == 0) {
                path.moveTo(x, y)
                continue
            }

            val controlX = x - stride / 2f
            val lastIndex = index - 1
            val lastY = yPoints.get(samples[lastIndex])
            path.cubicTo(
                controlX, lastY,
                controlX, y,
                x, y
            )
        }
        path.lineTo(rect.right, yPoints.get(samples.last()))

        // finish the line off to make a "chart" shape
        path.lineTo(rect.right, rect.bottom)
        path.lineTo(0f, rect.bottom)
        path.close()

        return@withContext path
    }

    private fun <C : Comparable<C>> List<C>.minMax(): Pair<C, C>? {
        var min = firstOrNull() ?: return null
        var max = min
        for (item in this) {
            if (min > item) min = item
            if (max < item) max = item
        }
        return min to max
    }

}

class PathCalculatorSampleCountSwitch<Sample>(
    private val sourceBelow: PathCalculator<Sample>,
    private val sourceAbove: PathCalculator<Sample>,
    private val sourceEqual: PathCalculator<Sample>,
    private val sampleCount: Int
) : PathCalculator<Sample> {

    override suspend fun getPath(samples: List<Sample>, rect: Rect): Path {
        if (rect.height == 0f)
            return sourceEqual.getPath(samples, rect)
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