@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.core

import cursola.core.util.nextCurrency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import kotlin.test.assertSame

class ExchangeRateRepositoryCacheTest {

    private lateinit var origin: ExchangeRateRepository
    private lateinit var repo: ExchangeRateRepositoryCache

    @Before
    fun prepare() {
        origin = mock()
        repo = ExchangeRateRepositoryCache(origin)
    }

    @Test
    fun getList_callsSource() = runTest {
        repo.get()
        verify(origin).get()
    }

    @Test
    fun getList_returns_cachedItem() = runTest {
        whenever(origin.get()).thenReturn(listOf(nextCurrency()))
        val firstResult = repo.get()
        val nextResult = repo.get()
        assertSame(firstResult, nextResult)
    }

    @Test
    fun getRate_callsSource() = runTest {
        val first = nextCurrency()
        val second = nextCurrency()
        repo.get(first, second)
        verify(origin).get(first, second)
    }

    @Test
    fun getRate_returns_cachedItem() = runTest {
        val first = nextCurrency()
        val second = nextCurrency()
        whenever(origin.get(first, second)).thenReturn(ExchangeRate(1.0))
        repo.get(first, second)
        verify(origin).get(first, second)
        repo.get(first, second)
        verifyNoMoreInteractions(origin)
    }

    @Test
    fun getRate_callsSource_withReversedOrder() = runTest {
        val first = nextCurrency()
        val second = nextCurrency()
        repo.get(first, second)
        verify(origin).get(first, second)
        repo.get(second, first)
        verify(origin).get(second, first)
    }

}
