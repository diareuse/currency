package cursola.core

import cursola.core.util.nextCurrency
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertSame

class ExchangeRateRepositoryRequireNotEmptyTest {

    private lateinit var repo: ExchangeRateRepositoryRequireNotEmpty
    private lateinit var origin: ExchangeRateRepository

    @Before
    fun prepare() {
        origin = mock()
        repo = ExchangeRateRepositoryRequireNotEmpty(origin)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getList_throws_onEmptyList() = runTest {
        whenever(origin.get()).thenReturn(emptyList())
        repo.get()
    }

    @Test
    fun getList_returns_onNonEmptyList() = runTest {
        val expected = listOf(nextCurrency())
        whenever(origin.get()).thenReturn(expected)
        val result = repo.get()
        assertSame(expected, result)
    }

}