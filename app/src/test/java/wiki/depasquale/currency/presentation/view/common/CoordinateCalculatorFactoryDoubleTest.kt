package wiki.depasquale.currency.presentation.view.common

import androidx.compose.ui.geometry.Rect
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.currency.tooling.MockableTest

class CoordinateCalculatorFactoryDoubleTest : MockableTest() {

    @Test
    fun `returns instance of double`() {
        val factory = CoordinateCalculatorFactoryDouble()
        val calculator = factory.build(ChartMetadata(Rect.Zero, 0.0, 0.0, 0))
        assertThat(calculator).isInstanceOf(CoordinateCalculatorDouble::class.java)
    }

}