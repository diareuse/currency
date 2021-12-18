package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

class PathCalculatorSampleCountSwitch<Sample>(
    private val sourceBelow: PathCalculator<Sample>,
    private val sourceAbove: PathCalculator<Sample>,
    private val sourceEqual: PathCalculator<Sample>,
    private val sampleCount: Int
) : PathCalculator<Sample> {

    override suspend fun getPath(samples: List<Sample>, rect: Rect): Path {
        return when (samples.size) {
            in Int.MIN_VALUE until sampleCount -> sourceBelow.getPath(samples, rect)
            sampleCount -> sourceEqual.getPath(samples, rect)
            else -> sourceAbove.getPath(samples, rect)
        }
    }

}