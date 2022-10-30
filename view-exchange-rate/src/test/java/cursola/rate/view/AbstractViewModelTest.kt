@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.rate.view

import androidx.lifecycle.ViewModel
import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.LatestValueDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Currency

internal abstract class AbstractViewModelTest<Model : ViewModel> {

    protected lateinit var history: HistoryDataSource
        private set
    protected lateinit var latest: LatestValueDataSource
        private set
    protected lateinit var conversion: ConversionRateDataSource
        private set
    protected lateinit var exchange: ExchangeRateDataSource
        private set
    protected lateinit var favorite: FavoriteDataSource
        private set
    protected lateinit var viewModel: Model
        private set

    @Before
    fun prepareInternal() {
        conversion = mock()
        exchange = mock()
        favorite = mock()
        history = mock()
        latest = mock()
        mockLatest()
        viewModel = prepare()
    }

    protected abstract fun prepare(): Model

    protected suspend fun mockFavorites() {
        val favorites = mutableListOf<Pair<Currency, Int>>()
        whenever(favorite.add(any(), any())).then {
            favorites.add(it.getArgument<Currency>(0) to it.getArgument(1))
        }
        whenever(favorite.remove(any())).then { invocation ->
            favorites.removeAll { it.first == invocation.getArgument(0) }
        }
        whenever(favorite.list()).thenAnswer {
            favorites.sortedByDescending { it.second }.map { it.first }
        }
    }

    protected fun mockLatest() {
        val values = mutableMapOf<String, String>()
        whenever(latest.currency).then { values.getOrDefault("currency", "EUR") }
        doAnswer { values["currency"] = it.getArgument(0) }.whenever(latest).currency = any()
        whenever(latest.value).then { values.getOrDefault("value", "1") }
        doAnswer { values["value"] = it.getArgument(0) }.whenever(latest).value = any()
    }

    protected fun TestScope.setMainDispatcher() {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
    }

    protected fun setMainDispatcher() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

}