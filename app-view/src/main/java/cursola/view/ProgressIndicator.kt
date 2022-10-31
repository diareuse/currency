package cursola.view

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    lineWidth: Dp = 4.dp,
    linePadding: Dp = 4.dp,
    tint: Color = LocalContentColor.current
) {
    val lineWidthPx = with(LocalDensity.current) { lineWidth.toPx() }
    val linePaddingPx = with(LocalDensity.current) { linePadding.toPx() }
    val transition = rememberInfiniteTransition()
    val first by transition.animateProgress(delay = 0)
    val second by transition.animateProgress(delay = 150)
    val third by transition.animateProgress(delay = 300)
    Canvas(modifier = modifier) {
        val center = size.center
        val left = center.copy(x = center.x - lineWidthPx / 2 - linePaddingPx)
        val right = center.copy(x = center.x + lineWidthPx / 2 + linePaddingPx)
        drawProgressLine(left, lineWidthPx, first, tint)
        drawProgressLine(center, lineWidthPx, second, tint)
        drawProgressLine(right, lineWidthPx, third, tint)
    }
}

@Composable
fun InfiniteTransition.animateProgress(delay: Int): State<Float> {
    return animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = EaseInOutSine
            ),
            initialStartOffset = StartOffset(delay, StartOffsetType.FastForward),
            repeatMode = RepeatMode.Reverse
        )
    )
}

private fun DrawScope.drawProgressLine(
    center: Offset,
    lineWidth: Float,
    progress: Float,
    tint: Color
) {
    val offset = size.height / 2 * progress
    val start = center.copy(y = lineWidth + offset)
    val end = center.copy(y = size.height - lineWidth - offset)
    drawLine(
        color = tint.copy(alpha = tint.alpha * progress),
        start = start,
        end = end,
        strokeWidth = lineWidth,
        cap = StrokeCap.Round,
        pathEffect = PathEffect.cornerPathEffect(size.minDimension)
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ProgressIndicator(
        modifier = Modifier.size(48.dp, 24.dp),
        lineWidth = 4.dp,
        linePadding = 4.dp
    )
}