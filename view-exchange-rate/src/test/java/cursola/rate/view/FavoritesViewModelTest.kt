package cursola.rate.view

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.util.Currency
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class FavoritesViewModelTest : AbstractViewModelTest<FavoritesViewModel>() {

    override fun prepare(): FavoritesViewModel {
        return FavoritesViewModel(conversion, favorite)
    }

    // ---

    @Test
    fun items_returns_defaultState_onNoValuesSet() = runTest {
        val currencies = Currency.getAvailableCurrencies().take(10)
        whenever(favorite.list()).thenReturn(currencies)
        whenever(conversion.get(eq(viewModel.selected.filterNotNull().first()), any())).thenReturn(
            12.0
        )
        val expected = currencies.map { ConvertedCurrency(it, 12.0, true) }
        val actual = viewModel.items.first()
        assertContentEquals(expected, actual)
    }

    @Test
    fun items_returns_updatedState_withoutSelectedCurrency() = runTest {
        val currencies = Currency.getAvailableCurrencies().take(10)
        val selected = currencies.random()
        whenever(favorite.list()).thenReturn(currencies)
        whenever(conversion.get(eq(selected), any())).thenReturn(12.0)
        viewModel.selected.value = selected
        val actual = viewModel.items.first()
        assertEquals(
            -1,
            actual.indexOfFirst { it.currency == selected },
            "Expected $selected not to be in $actual"
        )
    }

    @Test
    fun items_returns_updatedState_whenValueChanges() = runTest {
        val currencies = listOf(
            Currency.getInstance("USD"),
            Currency.getInstance("CZK")
        )
        whenever(favorite.list()).thenReturn(currencies)
        whenever(
            conversion.get(
                Currency.getInstance("EUR"),
                Currency.getInstance("USD")
            )
        ).thenReturn(12.0)
        whenever(
            conversion.get(
                Currency.getInstance("EUR"),
                Currency.getInstance("CZK")
            )
        ).thenReturn(5.0)
        viewModel.value.value = "32.3"
        val expected = listOf(
            ConvertedCurrency(Currency.getInstance("USD"), 387.59999999999997, true),
            ConvertedCurrency(Currency.getInstance("CZK"), 161.5, true),
        )
        val actual = viewModel.items.first()
        assertEquals(expected, actual)
    }

    @Test
    fun items_returns_updatedState_whenFavoritesChange() = runTest {
        suspend fun validateWith(currencies: List<Currency>) {
            whenever(conversion.get(any(), any())).thenReturn(1.0)
            val actual = viewModel.items.first()
            assertEquals(currencies.map { ConvertedCurrency(it, 1.0, true) }, actual)
        }

        var currencies = listOf(Currency.getInstance("USD"), Currency.getInstance("CZK"))
        whenever(favorite.list()).thenReturn(currencies)
        validateWith(currencies)
        currencies = listOf(Currency.getInstance("PHP"))
        whenever(favorite.list()).thenReturn(currencies)
        advanceTimeBy(1200)
        validateWith(currencies)
    }

}