package wiki.depasquale.currency.presentation.view.selection

import androidx.compose.ui.test.*
import org.junit.Test
import wiki.depasquale.currency.screen.selection.SelectionViewModel
import wiki.depasquale.currency.tooling.ComposeTest
import wiki.depasquale.currency.tooling.forEach
import wiki.depasquale.currency.tooling.forEachIndexed
import wiki.depasquale.currency.tooling.getCurrencies
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