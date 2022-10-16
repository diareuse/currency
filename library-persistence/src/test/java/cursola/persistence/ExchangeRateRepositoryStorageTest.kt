@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.persistence

import cursola.core.ExchangeRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ExchangeRateRepositoryStorageTest {

    private lateinit var repo: ExchangeRateRepositoryStorage
    private lateinit var rates: ExchangeRateDao
    private lateinit var currencies: CurrencyDao

    @Before
    fun prepare() {
        currencies = mock()
        rates = mock()
        repo = ExchangeRateRepositoryStorage(currencies, rates)
    }

    // ---

    @Test
    fun getList_calls_currencies() = runTest {
        repo.get()
        verify(currencies).select()
    }

    @Test
    fun getRate_calls_rates() = runTest {
        val currency = Currency.getInstance("USD")
        whenever(rates.select(currency.currencyCode, currency.currencyCode))
            .thenReturn(ExchangeRateSimple(1.0, Date()))
        repo.get(currency, currency)
        verify(rates).select(currency.currencyCode, currency.currencyCode)
    }

    @Test
    fun getList_returns_sameInstance() = runTest {
        val expected = Currency.getAvailableCurrencies().toList()
        whenever(currencies.select()).thenReturn(expected)
        val result = repo.get()
        assertSame(expected, result)
    }

    @Test
    fun getRate_returns_value() = runTest {
        val from = Currency.getInstance("USD")
        val to = Currency.getInstance("CZK")
        val expected = ExchangeRate(14.2)
        whenever(rates.select(from.currencyCode, to.currencyCode))
            .thenReturn(ExchangeRateSimple(expected.rate, expected.timestamp))
        val result = repo.get(from, to)
        assertEquals(expected, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getRate_throws_whenNull() = runTest {
        val from = Currency.getInstance("USD")
        val to = Currency.getInstance("CZK")
        whenever(rates.select(from.currencyCode, to.currencyCode)).thenReturn(null)
        repo.get(from, to)
    }

}