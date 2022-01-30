package wiki.depasquale.currency.presentation.view.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object ChartDefaults {

    val background = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 1f),
            Color.White.copy(alpha = .4f)
        )
    )

}