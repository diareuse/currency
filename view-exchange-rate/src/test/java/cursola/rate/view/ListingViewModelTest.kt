package cursola.rate.view

import cursola.rate.ExchangeRate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.whenever
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals

internal class ListingViewModelTest : AbstractViewModelTest<ListingViewModel>() {

    override fun prepare(): ListingViewModel {
        return ListingViewModel(exchange, favorite)
    }

    // ---

    @Test
    fun items_returns_exchangeValues() = runTest {
        mockFavorites()
        val expected = List(5) { makeExchangeRate() }
        whenever(exchange.get()).thenReturn(expected)
        val actual = viewModel.items.first()
        assertEquals(expected.map { ConvertedCurrency(it, false) }, actual)
    }

    @Test
    fun items_returns_sortedValues_byFavorites() = runTest {
        val rates = List(5) { makeExchangeRate() }
        val favorites = buildSet {
            repeat(3) { add(rates.random().currency) }
        }.toList()
        whenever(exchange.get()).thenReturn(rates)
        whenever(favorite.list()).thenReturn(favorites)
        val actual = viewModel.items.first()
        assertEquals(
            rates
                .map { ConvertedCurrency(it, it.currency in favorites) }
                .sortedByDescending { it.currency in favorites },
            actual
        )
    }

    @Test
    fun add_makesItems_return_updatedSet() = runTest {
        mockFavorites()
        setMainDispatcher()
        val rates = listOf(
            makeExchangeRate(currency = Currency.getInstance("USD")),
            makeExchangeRate(currency = Currency.getInstance("CZK"))
        )
        whenever(exchange.get()).thenReturn(rates)
        val favorite = Currency.getInstance("CZK")
        viewModel.add(favorite)
        val actual = viewModel.items.first()
        val expected = listOf(
            makeExchangeRate(currency = Currency.getInstance("CZK")),
            makeExchangeRate(currency = Currency.getInstance("USD"))
        ).map { ConvertedCurrency(it, it.currency == favorite) }
        assertEquals(expected, actual)
    }

    @Test
    fun remove_makesItems_return_updatedSet() = runTest {
        mockFavorites()
        setMainDispatcher()
        favorite.add(Currency.getInstance("CZK"), 0)
        val rates = listOf(
            makeExchangeRate(currency = Currency.getInstance("USD")),
            makeExchangeRate(currency = Currency.getInstance("CZK"))
        )
        whenever(exchange.get()).thenReturn(rates)
        viewModel.remove(Currency.getInstance("CZK"))
        val actual = viewModel.items.first()
        val expected = listOf(
            makeExchangeRate(currency = Currency.getInstance("USD")),
            makeExchangeRate(currency = Currency.getInstance("CZK"))
        ).map { ConvertedCurrency(it, false) }
        assertEquals(expected, actual)
    }

}

fun makeExchangeRate(
    currency: Currency = Currency.getAvailableCurrencies().random(),
    rate: Double = 1.0,
    timestamp: Date = Date(0)
) = ExchangeRate(currency, rate, timestamp)
