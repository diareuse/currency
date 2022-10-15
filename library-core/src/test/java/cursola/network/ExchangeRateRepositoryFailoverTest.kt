@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.network

import cursola.network.util.nextCurrency
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class ExchangeRateRepositoryFailoverTest {

    private lateinit var repo: ExchangeRateRepositoryFailover
    private lateinit var primary: ExchangeRateRepository
    private lateinit var secondary: ExchangeRateRepository

    @Before
    fun prepare() {
        primary = mock()
        secondary = mock()
        repo = ExchangeRateRepositoryFailover(primary, secondary)
    }

    @Test
    fun getList_returns_valueOfPrimary() = runTest {
        repo.get()
        verify(primary).get()
        verifyNoInteractions(secondary)
    }

    @Test
    fun getRate_returns_valueOfPrimary() = runTest {
        val first = nextCurrency()
        val second = nextCurrency()
        repo.get(first, second)
        verify(primary).get(first, second)
        verifyNoInteractions(secondary)
    }

    @Test
    fun getList_returns_valueOfSecondary() = runTest {
        whenever(primary.get()).thenThrow(RuntimeException())
        repo.get()
        verify(secondary).get()
    }

    @Test
    fun getRate_returns_valueOfSecondary() = runTest {
        val first = nextCurrency()
        val second = nextCurrency()
        whenever(primary.get(first, second)).thenThrow(RuntimeException())
        repo.get(first, second)
        verify(secondary).get(first, second)
    }

}