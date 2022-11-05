package cursola.view

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min

fun Modifier.topFadingEdge(
    length: Dp
): Modifier = composed {
    val fadingEdgeSize = with(LocalDensity.current) { length.toPx() }
    graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            val colors = listOf(Color.Transparent, Color.Black)
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(colors, startY = 0f, endY = fadingEdgeSize),
                blendMode = BlendMode.DstIn
            )
        }
}

fun Modifier.topFadingEdge(
    length: Dp,
    scrollState: LazyListState
): Modifier = composed {
    val firstIndex by remember { derivedStateOf { scrollState.firstVisibleItemIndex } }
    if (firstIndex == 0) {
        val scrollOffsetDp = with(LocalDensity.current) {
            scrollState.firstVisibleItemScrollOffset.toDp()
        }
        return@composed topFadingEdge(length = min(scrollOffsetDp, length))
    }
    topFadingEdge(length = length)
}