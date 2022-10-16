package cursola.persistence

import org.junit.Before
import org.junit.Test
import java.util.Currency
import kotlin.test.assertEquals

class ConverterCurrencyTest {

    private lateinit var converter: ConverterCurrency

    @Before
    fun prepare() {
        converter = ConverterCurrency()
    }

    @Test
    fun from_returnsInstance() {
        val expected = Currency.getAvailableCurrencies().random()
        assertEquals(expected, converter.from(expected.currencyCode))
    }

    @Test
    fun to_returnsCode() {
        val expected = Currency.getAvailableCurrencies().random()
        assertEquals(expected.currencyCode, converter.to(expected))
    }

}