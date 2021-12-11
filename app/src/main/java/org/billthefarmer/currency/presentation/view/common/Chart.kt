package org.billthefarmer.currency.presentation.view.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    pathCalculator: PathCalculator = ChartDefaults.pathCalculator,
    background: Brush = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 1f),
            Color.White.copy(alpha = .4f)
        )
    ),
    samples: List<Double>
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
            })
}