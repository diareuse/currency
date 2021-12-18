package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

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