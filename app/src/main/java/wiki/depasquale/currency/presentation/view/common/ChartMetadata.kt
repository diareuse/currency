package wiki.depasquale.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect

data class ChartMetadata<Sample>(
    val container: Rect,
    val minY: Sample,
    val maxY: Sample,
    val count: Int
)