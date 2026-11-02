package wiki.depasquale.currency

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import cursola.rate.convert.Conversion
import cursola.rate.convert.CurrencyInfo
import cursola.rate.convert.Money
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import java.math.BigDecimal
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
class ConversionGraphTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(SNAPSHOT)),
            ExchangeRate(USD, 1.25, Date(SNAPSHOT)),
            ExchangeRate(CZK, 25.0, Date(SNAPSHOT)),
        )
    }

    @Test
    fun app_converter_converts_with_graph_rates_and_snapshot_timestamp() = runTest {
        val actual = app.appGraph.converter.convert(money("10", EUR), USD)

        assertEquals(Conversion(money("10", EUR), money("12.5", USD), 1.25, Date(SNAPSHOT)), actual)
    }

    @Test
    fun app_converter_converts_to_stored_favorites() = runTest {
        graph.favorites.add(CZK, 1)
        graph.favorites.add(USD, 0)

        val actual = app.appGraph.converter.convertToFavorites(money("2", EUR)).map { it.result }

        assertEquals(listOf(money("50", CZK), money("2.5", USD)), actual)
    }

    @Test
    fun app_amount_parser_reads_codes_and_symbols() {
        val parser = app.appGraph.amountParser

        assertEquals(money("12", USD), parser.parse("12 USD", Locale.US))
        assertEquals(money("30", EUR), parser.parse("€30", Locale.US))
        assertEquals(money("1234.5", EUR), parser.parse("1,234.5 eur", Locale.US))
        assertNull(parser.parse("twelve", Locale.US))
    }

    @Test
    fun app_rates_query_lists_currencies_and_latest_rates() = runTest {
        val rates = app.appGraph.ratesQuery

        assertEquals(CurrencyInfo(USD, "US Dollar", "$"), rates.currencies(Locale.US)[1])
        assertEquals(listOf(money("0.8", EUR), money("20", CZK)), rates.latest(USD).map { it.result })
    }

    private fun money(amount: String, currency: Currency) = Money(BigDecimal(amount), currency)

    private companion object {
        const val SNAPSHOT = 1_700_000_000_000L
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val CZK: Currency = Currency.getInstance("CZK")
    }

}
