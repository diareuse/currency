package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PathCalculatorDouble(
    private val factory: CoordinateCalculatorFactory<Double>
) : PathCalculator<Double> {

    override suspend fun getPath(
        samples: List<Double>,
        rect: Rect
    ): Path = withContext(Dispatchers.IO) {
        val metadata = ChartMetadata(
            container = rect,
            minY = samples.minOrNull() ?: 0.0,
            maxY = samples.maxOrNull() ?: 0.0,
            count = samples.size
        )
        val coordinates = factory.build(metadata)
        val path = Path()

        for ((index, sample) in samples.withIndex()) {
            val x = coordinates.getX(sample, index)
            val y = coordinates.getY(sample, index)
            if (index == 0) {
                path.moveTo(x, y)
                continue
            }

            val controlX = x - coordinates.strideX / 2f
            val lastIndex = index - 1
            val lastY = coordinates.getY(samples[lastIndex], lastIndex)
            path.cubicTo(
                controlX, lastY,
                controlX, y,
                x, y
            )
        }
        path.lineTo(rect.right, coordinates.getY(samples.last(), samples.size - 1))

        // finish the line off to make a "chart" shape
        path.lineTo(rect.right, rect.bottom)
        path.lineTo(0f, rect.bottom)
        path.close()

        return@withContext path
    }

}