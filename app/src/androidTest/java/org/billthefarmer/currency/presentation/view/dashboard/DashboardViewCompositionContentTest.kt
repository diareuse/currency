package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.ui.test.*
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.tooling.forEach
import org.billthefarmer.currency.tooling.forEachIndexed
import org.billthefarmer.currency.tooling.getCurrencies
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import org.junit.Test
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
        val nodes = onAllNodesWithTag("content-currency-value")
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

}