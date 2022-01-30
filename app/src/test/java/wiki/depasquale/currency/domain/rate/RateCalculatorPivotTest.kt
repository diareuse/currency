package wiki.depasquale.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.tooling.MockableTest
import java.util.*

class RateCalculatorPivotTest : MockableTest() {

    private lateinit var calculator: RateCalculatorPivot
    private var value: Double = Double.NaN
    private lateinit var pivot: ExchangeRate

    override fun prepare() {
        super.prepare()
        pivot = ExchangeRate(Currency.getInstance("USD"), 12.3, Date(0))
        value = 23.4
        calculator = RateCalculatorPivot(pivot, value)
    }

    @Test
    fun `returns translated value`() {
        val output = calculator.getAdjustedRate(300.0)
        assertThat(output).isWithin(0.01).of(570.7317073171)
    }

}