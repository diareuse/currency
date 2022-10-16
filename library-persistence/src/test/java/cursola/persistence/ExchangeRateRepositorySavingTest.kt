@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.persistence

import cursola.core.ExchangeRate
import cursola.core.ExchangeRateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Currency

class ExchangeRateRepositorySavingTest {

    private lateinit var repo: ExchangeRateRepositorySaving
    private lateinit var origin: ExchangeRateRepository
    private lateinit var currencies: CurrencyDao
    private lateinit var rates: ExchangeRateDao

    @Before
    fun prepare() {
        origin = mock()
        currencies = mock()
        rates = mock()
        repo = ExchangeRateRepositorySaving(origin, currencies, rates)
    }

    // ---

    @Test
    fun getList_calls_origin() = runTest {
        whenever(origin.get()).thenReturn(emptyList())
        repo.get()
        verify(origin).get()
    }

    @Test
    fun getRate_calls_origin() = runTest {
        val currency = Currency.getInstance("USD")
        whenever(origin.get(currency, currency)).thenReturn(ExchangeRate(1.0))
        repo.get(currency, currency)
        verify(origin).get(currency, currency)
    }

    @Test
    fun getList_calls_currencyInsert() = runTest {
        val data = Currency.getAvailableCurrencies().toList()
        val expected = data.map { SupportedCurrency(it) }
        whenever(origin.get()).thenReturn(data)
        repo.get()
        verify(currencies).insert(expected)
    }

    @Test
    fun getRate_calls_ratesInsert() = runTest {
        val from = Currency.getInstance("USD")
        val to = Currency.getInstance("CZK")
        val rate = ExchangeRate(1.0)
        val expected = ExchangeRatePersisted(from, to, rate.rate, rate.timestamp)
        whenever(origin.get(from, to)).thenReturn(rate)
        repo.get(from, to)
        verify(rates).insert(expected)
    }

}