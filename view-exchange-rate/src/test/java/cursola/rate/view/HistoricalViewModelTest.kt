package cursola.rate.view

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.whenever
import java.util.Currency
import java.util.Date
import kotlin.random.Random.Default.nextDouble
import kotlin.test.assertEquals

internal class HistoricalViewModelTest : AbstractViewModelTest<HistoricalViewModel>() {

    override fun prepare(): HistoricalViewModel {
        setMainDispatcher()
        return HistoricalViewModel(favorite, history)
    }

    // ---

    @Test
    fun items_emit_valuesFromFavorites() = runTest {
        val currency = Currency.getInstance("USD")
        val rates = List(5) {
            makeExchangeRate(
                currency = currency,
                rate = nextDouble(1.0, 1000.0),
                timestamp = Date(it.toLong())
            )
        }
        whenever(favorite.list()).thenReturn(listOf(currency))
        whenever(history.get(currency)).thenReturn(rates)
        val result = viewModel.items.first()
        val expected = HistoricalViewModel
            .HistoryValues(currency, rates.map(HistoricalViewModel::ChartValue))
            .let(::listOf)
        assertEquals(expected, result)
    }

}