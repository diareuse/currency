@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.network

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
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ExchangeRateRepositoryNetworkTest {

    private lateinit var service: RatesService
    private lateinit var repo: ExchangeRateRepositoryNetwork

    @Before
    fun prepare() {
        service = mock()
        repo = ExchangeRateRepositoryNetwork(service)
    }

    @Test
    fun getList_calls_service() = runTest {
        whenever(service.get()).thenReturn(emptyList())
        repo.get()
        verify(service).get()
    }

    @Test
    fun getList_returns_value() = runTest {
        val expected = Currency.getAvailableCurrencies()
        whenever(service.get()).thenReturn(expected.map { makeResponse(currency = it) })
        val result = repo.get()
        assertContentEquals(expected, result)
    }

    @Test
    fun getRate_call_service() = runTest {
        whenever(service.get()).thenReturn(
            Currency.getAvailableCurrencies().toList().map { makeResponse(it) })
        repo.get(Currency.getInstance("USD"), Currency.getInstance("USD"))
        verify(service).get()
    }

    @Test
    fun getRate_returns_value() = runTest {
        val currency = Currency.getInstance("USD")
        whenever(service.get()).thenReturn(listOf(currency).map { makeResponse(it, 1.0) })
        val result = repo.get(currency, currency)
        assertEquals(ExchangeRate(1.0, Date(0)), result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getRate_throws_whenFromNotFound() = runTest {
        whenever(service.get()).thenReturn(listOf(Currency.getInstance("USD")).map { makeResponse(it) })
        repo.get(Currency.getInstance("CZK"), Currency.getInstance("USD"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getRate_throws_whenToNotFound() = runTest {
        whenever(service.get()).thenReturn(listOf(Currency.getInstance("USD")).map { makeResponse(it) })
        repo.get(Currency.getInstance("USD"), Currency.getInstance("CZK"))
    }

}