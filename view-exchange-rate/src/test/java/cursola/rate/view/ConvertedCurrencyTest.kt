package cursola.rate.view

import org.junit.Test
import java.util.Currency
import java.util.Locale
import kotlin.test.assertEquals

internal class ConvertedCurrencyTest {

    @Test
    fun name_returnsLocalizedName() {
        val converted = ConvertedCurrency(Currency.getInstance("USD"), 1.0, false)
        assertEquals("US Dollar", converted.name(Locale.US))
    }

    @Test
    fun symbol_returnsSymbol() {
        val converted = ConvertedCurrency(Currency.getInstance("USD"), 1.0, false)
        assertEquals("$", converted.symbol(Locale.US))
    }

    @Test
    fun symbol_returnsCode_whenSymbolUnknown() {
        val converted = ConvertedCurrency(Currency.getInstance("CZK"), 1.0, false)
        assertEquals("CZK", converted.symbol(Locale.US))
    }

    @Test
    fun toString_returnsFormattedString_roundsUp() {
        val currency = ConvertedCurrency(Currency.getInstance("CZK"), 123.4451, false)
        val actual = currency.toString(Locale.US)
        assertEquals("CZK123.45", actual)
    }

    @Test
    fun toString_returnsFormattedString_roundsDown() {
        val currency = ConvertedCurrency(Currency.getInstance("CZK"), 123.444, false)
        val actual = currency.toString(Locale.US)
        assertEquals("CZK123.44", actual)
    }

    @Test
    fun constructor_transformsRate() {
        val rate = makeExchangeRate()
        val actual = ConvertedCurrency(rate, false)
        val expected = ConvertedCurrency(currency = rate.currency, value = rate.rate, false)
        assertEquals(expected, actual)
    }

}