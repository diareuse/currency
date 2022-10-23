package cursola.rate

import android.database.sqlite.SQLiteConstraintException
import cursola.rate.database.ExchangeRateStored
import cursola.rate.di.ExchangeRateModule
import cursola.rate.model.makeExchangeRate
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.whenever
import java.io.IOException
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class ExchangeRateDataSourceTest : AbstractDataSourceTest() {

    private lateinit var source: ExchangeRateDataSource

    override fun prepare() {
        source = ExchangeRateModule.getInstance().exchangeRate(network, database)
    }

    // ---

    @Test
    fun get_returns_dataFromNetwork() = runTest {
        val data = prepareTest(10)
        whenever(network.get()).thenReturn(data)
        val result = source.get()
        for (item in data)
            assertContains(result, item)
    }

    @Test
    fun get_returns_dataFromDatabase() = runTest {
        val data = prepareTest(10)
        whenever(network.get()).thenThrow(IOException())
        val rates = database.rates()
        whenever(rates.getLatest()).thenReturn(data.map(::ExchangeRateStored))
        val result = source.get()
        assertContentEquals(data, result)
    }

    @Test
    fun get_returns_emptyList_whenResourcesFail() = runTest {
        val rates = database.rates()
        whenever(rates.getLatest()).thenThrow(RuntimeException())
        whenever(network.get()).thenThrow(IOException())
        val result = source.get()
        assertEquals(emptyList(), result)
    }

    @Test
    fun get_saves_toDatabase() = runTest {
        val data = prepareTest(1)
        whenever(network.get()).thenReturn(data)
        source.get()
        database.rates().inOrder {
            verify().insert(data.map(::ExchangeRateStored).first())
        }
    }

    @Test
    fun get_updates_toDatabase() = runTest {
        val data = prepareTest(1)
        val rates = database.rates()
        whenever(network.get()).thenReturn(data)
        whenever(rates.insert(model = any())).thenThrow(SQLiteConstraintException())
        source.get()
        database.rates().inOrder {
            verify().update(data.map(::ExchangeRateStored).first())
        }
    }

    @Test
    fun get_returns_distinctItems_fromNetwork() = runTest {
        val item = makeExchangeRate()
        val data = List(10) { item }
        whenever(network.get()).thenReturn(data)
        val actual = source.get()
        assertEquals(listOf(item), actual)
    }

    @Test
    fun get_returns_distinctItems_fromDatabase() = runTest {
        val item = makeExchangeRate()
        val data = List(10) { item }
        whenever(network.get()).thenThrow(IOException())
        whenever(database.rates().getLatest()).thenReturn(data.map(::ExchangeRateStored))
        val actual = source.get()
        assertEquals(listOf(item), actual)
    }

    // ---

    private fun prepareTest(size: Int): List<ExchangeRate> = buildList {
        while (this.size < size) {
            val rate = makeExchangeRate()
            if (rate.currency in map { it.currency })
                continue
            this += rate
        }
    }

}