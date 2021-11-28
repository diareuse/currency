package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.ui.test.*
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.tooling.forEach
import org.billthefarmer.currency.tooling.forEachIndexed
import org.billthefarmer.currency.tooling.getCurrencies
import org.billthefarmer.currency.ui.selection.SelectionViewModel
import org.junit.Test
import kotlin.random.Random

class SelectionViewCompositionContentTest : ComposeTest() {

    private lateinit var viewModel: SelectionViewModel
    private lateinit var view: SelectionViewComposition

    override fun prepare() {
        viewModel = SelectionViewModel()
        view = SelectionViewCompositionContent()
    }

    @Test
    fun contains_sameAmountOfItems() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = Random.nextInt(0, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onAllNodesWithTag("content-exchange-rate-holder")
            .assertCountEquals(viewModel.currencies.value.size)
    }

    @Test
    fun contains_allCurrencyNames() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = Random.nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = Random.nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = Random.nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = Random.nextInt(0, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-exchange-rate-holder")
        nodes.forEach {
            it.assertHasClickAction()
        }
    }

}