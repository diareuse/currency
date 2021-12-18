package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test

class CoordinateCalculatorDoubleTest : MockableTest() {

    @Test
    fun `returns uniform stride`() {
        val count = 10
        val metadata = getMetadata(900f, count = count)
        val calculator = CoordinateCalculatorDouble(metadata)
        val stride = calculator.strideX
        assertThat(stride).isEqualTo(100f)
    }

    @Test
    fun `returns first x coordinate`() {
        val metadata = getMetadata(size = 900f, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val x = calculator.getX(0.0, 0)
        assertThat(x).isEqualTo(-450f)
    }

    @Test
    fun `returns middle x coordinate`() {
        val metadata = getMetadata(size = 900f, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val x = calculator.getX(0.0, 4)
        assertThat(x).isEqualTo(-50f)
    }

    @Test
    fun `returns last x coordinate`() {
        val metadata = getMetadata(size = 900f, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val x = calculator.getX(0.0, 9)
        assertThat(x).isEqualTo(450f)
    }

    @Test
    fun `returns top y coordinate`() {
        val metadata = getMetadata(size = 900f, minY = 0.0, maxY = 1.0, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val y = calculator.getY(0.0, 0)
        assertThat(y).isEqualTo(900f)
    }

    @Test
    fun `returns middle y coordinate`() {
        val metadata = getMetadata(size = 900f, minY = 0.0, maxY = 1.0, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val y = calculator.getY(0.5, 0)
        assertThat(y).isEqualTo(450f)
    }

    @Test
    fun `returns bottom y coordinate`() {
        val metadata = getMetadata(size = 900f, minY = 0.0, maxY = 1.0, count = 10)
        val calculator = CoordinateCalculatorDouble(metadata)
        val y = calculator.getY(1.0, 0)
        assertThat(y).isEqualTo(0f)
    }

    // ---

    private fun getMetadata(
        size: Float = -1f,
        minY: Double = 0.0,
        maxY: Double = 0.0,
        count: Int = 0
    ) = ChartMetadata(
        container = Rect(Offset.Zero, size / 2f),
        minY = minY,
        maxY = maxY,
        count = count
    )

}