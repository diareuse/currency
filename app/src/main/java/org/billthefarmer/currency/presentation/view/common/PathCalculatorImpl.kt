package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

class PathCalculatorImpl : PathCalculator {
    override suspend fun getPath(
        samples: List<Double>,
        rect: Rect
    ): Path = withContext(Dispatchers.Default) {
        val path = Path()
        if (samples.size < 2) {
            path.moveTo(rect.left, rect.center.y)
            path.lineTo(rect.right, rect.center.y)
            path.lineTo(rect.right, rect.bottom)
            path.lineTo(0f, rect.bottom)
            path.close()
            return@withContext path
        }

        val width = rect.width
        val height = rect.height

        val min = samples.minOrNull() ?: 0.0
        val max = samples.maxOrNull() ?: 0.0

        val widthSteps = (width / max(1, samples.size - 1))

        fun yCoord(sample: Double) =
            height * (1 - ((sample - min) / (max - min))).toFloat()

        for ((index, sample) in samples.withIndex()) {
            val x = index * widthSteps + rect.left
            val y = yCoord(sample)
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                val controlX = x - widthSteps / 2f
                val lastY = yCoord(samples[index - 1])
                path.cubicTo(
                    controlX, lastY,
                    controlX, y,
                    x, y
                )
            }
        }
        path.lineTo(rect.right, yCoord(samples.last()))

        // finish the line off to make a "chart" shape
        path.lineTo(rect.right, rect.bottom)
        path.lineTo(0f, rect.bottom)
        path.close()

        return@withContext path
    }
}