package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.ui.test.*
import org.junit.Test
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.CurrencyModel
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.tooling.ComposeTest
import wiki.depasquale.currency.tooling.forEach
import wiki.depasquale.currency.tooling.forEachIndexed
import wiki.depasquale.currency.tooling.getCurrencies
import java.util.*
import kotlin.random.Random.Default.nextInt

class DashboardViewCompositionContentTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionContent

    override fun prepare() {
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionContent()
    }

    @Test
    fun contains_sameAmountOfItems() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(1, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onAllNodesWithTag("content-exchange-rate-holder")
            .assertCountEquals(viewModel.currencies.value.size)
    }

    @Test
    fun contains_allCurrencyNames() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(1, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-currency-name")
        val currencies = viewModel.currencies.value
        nodes.forEachIndexed { index, it ->
            val text = currencies[index].rate.currency.displayName
            it.assertTextContains(text)
        }
    }

    @Test
    fun contains_allCurrencyValues() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(1, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-currency-value")
        val currencies = viewModel.currencies.value
        nodes.forEachIndexed { index, it ->
            val text = "%.2f".format(currencies[index].rate.rate)
            it.assertTextContains(text)
        }
    }

    @Test
    fun contains_allCurrencyFlags() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(1, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-flag")
        val currencies = viewModel.currencies.value
        nodes.forEachIndexed { index, it ->
            val text = currencies[index].rate.currency.displayName
            it.assertContentDescriptionContains(text)
        }
    }

    @Test
    fun holder_hasClickAction() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(1, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-exchange-rate-holder")
        nodes.forEach {
            it.assertHasClickAction()
        }
    }

    @Test
    fun containsNot_selectedItem() = inCompose(
        before = {
            val currencies = getCurrencies(count = 4)
            viewModel.selectedCurrency.value = currencies.random()
            viewModel.currencies.value = currencies
        }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val value = requireNotNull(viewModel.selectedCurrency.value)
        onNodeWithText(value.rate.currency.currencyCode).assertDoesNotExist()
        onNodeWithText(value.rate.currency.displayName).assertDoesNotExist()
    }

    @Test
    fun contains_calculatedValues() = inCompose(
        before = {
            val currencies = listOf(
                CurrencyModel(
                    ExchangeRate(
                        currency = Currency.getInstance("USD"),
                        rate = 1.13,
                        time = Date()
                    )
                )
            )
            viewModel.currencyPivot.value = ExchangeRate(
                currency = Currency.getInstance("EUR"),
                rate = 1.0,
                time = Date()
            )
            viewModel.selectedCurrency.value = CurrencyModel(
                ExchangeRate(
                    currency = Currency.getInstance("PHP"),
                    rate = 57.08,
                    time = Date()
                )
            )
            viewModel.currencies.value = currencies
            viewModel.amount.value = "5000"
        }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-currency-value")
        nodes.forEach {
            it.assertTextContains("98.98")
        }
    }

}