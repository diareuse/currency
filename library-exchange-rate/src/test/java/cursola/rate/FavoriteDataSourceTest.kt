package cursola.rate

import cursola.rate.di.ExchangeRateModule
import cursola.rate.model.makeFavorite
import cursola.rate.util.nextCurrency
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertContentEquals

internal class FavoriteDataSourceTest : AbstractDataSourceTest() {

    private lateinit var source: FavoriteDataSource

    override fun prepare() {
        source = ExchangeRateModule.getInstance().favorites(database)
    }

    // ---

    @Test
    fun list_returns_savedCurrencies() = runTest {
        val favorites = database.favorites()
        val currency = listOf(nextCurrency())
        whenever(favorites.get()).thenReturn(currency.map(::makeFavorite))
        val result = source.list()
        assertContentEquals(currency, result)
    }

    @Test
    fun add_writesToDatabase() = runTest {
        val favorites = database.favorites()
        val currency = nextCurrency()
        source.add(currency, 0)
        verify(favorites).insert(makeFavorite(currency, 0))
    }

    @Test
    fun add_updatesDatabase() = runTest {
        val favorites = database.favorites()
        val currency = nextCurrency()
        whenever(favorites.insert(makeFavorite(currency, 0))).thenThrow(IllegalStateException())
        source.add(currency, 0)
        verify(favorites).update(makeFavorite(currency, 0))
    }

    @Test
    fun remove_updatesDatabase() = runTest {
        val favorites = database.favorites()
        val currency = nextCurrency()
        source.remove(currency)
        verify(favorites).delete(makeFavorite(currency, 0))
    }

}