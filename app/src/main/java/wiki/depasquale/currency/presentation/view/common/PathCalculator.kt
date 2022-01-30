package wiki.depasquale.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

interface PathCalculator<Sample> {

    suspend fun getPath(samples: List<Sample>, rect: Rect): Path

}
