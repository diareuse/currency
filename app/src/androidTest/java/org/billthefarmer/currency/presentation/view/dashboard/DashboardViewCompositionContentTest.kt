package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.ui.test.*
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.tooling.CurrencyPool
import org.billthefarmer.currency.tooling.forEach
import org.billthefarmer.currency.tooling.forEachIndexed
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import org.junit.Test
import java.util.*
import kotlin.random.Random.Default.nextDouble
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
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(0, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onAllNodesWithTag("content-exchange-rate-holder")
            .assertCountEquals(viewModel.currencies.value.size)
    }

    @Test
    fun contains_allCurrencyNames() = inCompose(
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(0, 10)) }
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
        before = { viewModel.currencies.value = getCurrencies(count = nextInt(0, 10)) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        val nodes = onAllNodesWithTag("content-exchange-rate-holder")
        nodes.forEach {
            it.assertHasClickAction()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getCurrencies(count: Int): List<CurrencyModel> = buildList {
        repeat(count) {
            this += getModel()
        }
    }

    private fun getModel() = CurrencyModel(
        getRate()
    )

    private fun getRate(): ExchangeRate {
        return ExchangeRate(CurrencyPool.random(), nextDouble(0.0, Double.MAX_VALUE), Date())
    }

}