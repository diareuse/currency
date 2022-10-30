package cursola.rate

import cursola.rate.database.ExchangeRateStored
import cursola.rate.di.ExchangeRateModule
import cursola.rate.model.makeExchangeRate
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import java.util.Currency
import java.util.Date
import kotlin.test.assertContains
import kotlin.test.assertEquals

internal class HistoryDataSourceTest : AbstractDataSourceTest() {

    private lateinit var source: HistoryDataSource

    override fun prepare() {
        source = ExchangeRateModule().historyValue(network, database)
    }

    // ---

    @Test
    fun get_returns_dataFromNetwork() = runTest {
        val currency = Currency.getInstance("USD")
        val items = List(10) { makeExchangeRate(currency = currency) }
        whenever(network.get()).thenReturn(items)
        val result = source.get(currency)
        for (item in items)
            assertContains(result, item)
    }

    @Test
    fun get_returns_dataFromNetwork_onlySelectedCurrency() = runTest {
        val currency = Currency.getInstance("USD")
        val items = List(10) { makeExchangeRate() } + makeExchangeRate(currency = currency)
        whenever(network.get()).thenReturn(items)
        val result = source.get(currency)
        assertEquals(
            expected = 1,
            actual = result.distinctBy { it.currency.currencyCode }.size,
            message = "Expected to contain only $currency, but was ${result.map { it.currency }}"
        )
    }

    @Test
    fun get_returns_dataFromDatabase_whenNetworkFails() = runTest {
        val currency = Currency.getInstance("USD")
        val items = List(10) { ExchangeRateStored(makeExchangeRate(currency = currency)) }
        val rates = database.rates()
        whenever(rates.getAll(currency.currencyCode)).thenReturn(items)
        whenever(network.get()).thenThrow(IOException())
        val result = source.get(currency)
        for (item in items)
            assertContains(result, ExchangeRate(item))
    }

    @Test
    fun get_returns_dataFromDatabase_whenNetworkEmpty() = runTest {
        val currency = Currency.getInstance("USD")
        val items = List(10) { ExchangeRateStored(makeExchangeRate(currency = currency)) }
        val rates = database.rates()
        whenever(rates.getAll(currency.currencyCode)).thenReturn(items)
        whenever(network.get()).thenReturn(emptyList())
        val result = source.get(currency)
        for (item in items)
            assertContains(result, ExchangeRate(item))
    }

    @Test
    fun get_fromNetwork_savesAllEntriesToDatabase() = runTest {
        // specifically selected currency without pegs
        val currency = Currency.getInstance("CZK")
        val items = List(10) { makeExchangeRate(currency = currency) }
        whenever(network.get()).thenReturn(items)
        source.get(currency)
        val rates = database.rates()
        verify(rates, times(10)).insert(any<ExchangeRateStored>())
    }

    @Test
    fun get_fromNetwork_savesEntriesCorrectly() = runTest {
        val rates = database.rates()
        val currency = Currency.getInstance("USD")
        val items = List(1) { makeExchangeRate(currency = currency) }
        whenever(network.get()).thenReturn(items)
        source.get(currency)
        verify(rates).insert(ExchangeRateStored(items[0]))
    }

    @Test
    fun get_forBaseline_returnsSingleValue() = runTest {
        val currency = Currency.getInstance("EUR")
        val result = source.get(currency)
        assertEquals(listOf(makeExchangeRate(currency, 1.0)), result)
    }

    @Test
    fun get_forPeg_returnsMappedValues() = runTest {
        val currency = Currency.getInstance("AED")
        val usdRates = List(10) {
            makeExchangeRate(
                Currency.getInstance("USD"),
                timestamp = Date(10 - it.toLong())
            )
        }
        whenever(network.get()).thenReturn(usdRates)
        val result = source.get(currency)
        assertEquals(usdRates.map { it.copy(currency = currency, rate = 3.6725 * it.rate) }, result)
    }

}