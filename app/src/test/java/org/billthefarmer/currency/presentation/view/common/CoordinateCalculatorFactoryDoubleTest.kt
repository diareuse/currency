package org.billthefarmer.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test

class CoordinateCalculatorFactoryDoubleTest : MockableTest() {

    @Test
    fun `returns instance of double`() {
        val factory = CoordinateCalculatorFactoryDouble()
        val calculator = factory.build(ChartMetadata(Rect.Zero, 0.0, 0.0, 0))
        assertThat(calculator).isInstanceOf(CoordinateCalculatorDouble::class.java)
    }

}