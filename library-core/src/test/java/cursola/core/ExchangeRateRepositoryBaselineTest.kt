@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Currency
import kotlin.test.assertContentEquals

class ExchangeRateRepositoryBaselineTest {

    private lateinit var currency: Currency
    private lateinit var origin: ExchangeRateRepository
    private lateinit var repo: ExchangeRateRepositoryBaseline

    @Before
    fun prepare() {
        origin = mock()
        currency = Currency.getInstance("USD")
        repo = ExchangeRateRepositoryBaseline(origin, currency)
    }

    @Test
    fun get_prependsBaseline_whenEmpty() = runTest {
        whenever(origin.get()).thenReturn(emptyList())
        assertContentEquals(listOf(currency), repo.get())
    }

    @Test
    fun get_prependsBaseline_whenFull() = runTest {
        val expected = Currency.getAvailableCurrencies().toList()
        whenever(origin.get()).thenReturn(expected)
        val result = repo.get()
        assertContentEquals(result, listOf(currency) + expected)
    }

}