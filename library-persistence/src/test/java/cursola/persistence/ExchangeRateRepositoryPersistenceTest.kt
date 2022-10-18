@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.persistence

import cursola.core.ExchangeRate
import cursola.core.ExchangeRateRepository
import cursola.persistence.database.StorageImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals

class ExchangeRateRepositoryPersistenceTest {

    private lateinit var database: Storage
    private lateinit var origin: ExchangeRateRepository
    private lateinit var persistence: ExchangeRateRepositoryPersistence

    @Before
    fun prepare() {
        database = StorageImpl()
        origin = mock()
        persistence = ExchangeRateRepositoryPersistence(database)
        runBlocking {
            whenever(origin.get()).thenReturn(Currency.getAvailableCurrencies().toList())
            whenever(origin.get(any(), any())).thenReturn(makeExchangeRate())
        }
    }

    @Test
    fun wrap_get_savesIntoDatabase() = runTest {
        val repo = persistence.wrap(origin)
        val expected = repo.get()
        val result = database.currency().select()
        assertEquals(expected, result)
    }

    @Test
    fun wrap_getRate_savesIntoDatabase() = runTest {
        val from = makeCurrency()
        val to = makeCurrency()
        val repo = persistence.wrap(origin)
        val expected = repo.get(from, to)
        val result = database.rates().select(from.currencyCode, to.currencyCode)
        assertEquals(expected.rate, result?.rate)
        assertEquals(expected.timestamp, result?.timestamp)
    }

    @Test
    fun storage_get_readsFromDatabase() = runTest {
        val expected = listOf(makeCurrency())
        whenever(database.currency().select()).thenReturn(expected)
        val repo = persistence.storage()
        val result = repo.get()
        assertEquals(expected, result)
    }

    @Test
    fun storage_getRate_readsFromDatabase() = runTest {
        val from = makeCurrency()
        val to = makeCurrency()
        val expected = makeSimpleRate()
        whenever(database.rates().select(from.currencyCode, to.currencyCode)).thenReturn(expected)
        val repo = persistence.storage()
        val result = repo.get(from, to)
        assertEquals(expected.rate, result.rate)
        assertEquals(expected.timestamp, result.timestamp)
    }

    @Test(expected = RuntimeException::class)
    fun storage_getRate_throwsWhenNotFound() = runTest {
        val from = makeCurrency()
        val to = makeCurrency()
        whenever(database.rates().select(from.currencyCode, to.currencyCode))
            .thenThrow(RuntimeException())
        val repo = persistence.storage()
        repo.get(from, to)
    }

    // ---

    private fun makeExchangeRate() = ExchangeRate(1.0)
    private fun makeSimpleRate() = ExchangeRateSimple(1.0, Date())
    private fun makeCurrency() = Currency.getAvailableCurrencies().random()

}