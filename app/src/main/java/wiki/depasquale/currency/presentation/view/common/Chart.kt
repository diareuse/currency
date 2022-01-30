package wiki.depasquale.currency.presentation.view.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged

@Composable
fun <Sample> Chart(
    samples: List<Sample>,
    pathCalculator: PathCalculator<Sample>,
    modifier: Modifier = Modifier,
    background: Brush = ChartDefaults.background
) {
    var path by remember { mutableStateOf(Path()) }
    var size by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(samples, size) {
        path = pathCalculator.getPath(samples, size)
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
    modifier: Modifier = Modifier,
    background: Brush = ChartDefaults.background
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