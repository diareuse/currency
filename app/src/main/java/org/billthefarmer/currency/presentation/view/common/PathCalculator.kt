package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

interface PathCalculator {

    suspend fun getPath(samples: List<Double>, rect: Rect): Path

}
