package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import org.junit.Test
import java.util.*

class DashboardViewCompositionCurrencyForkTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionCurrencyFork
    private lateinit var onSelected: DashboardViewComposition
    private lateinit var onMissing: DashboardViewComposition

    private var called = false

    override fun prepare() {
        viewModel = DashboardViewModel()
        onMissing = ViewComposition {
            Box(modifier = Modifier.testTag("called-missing"))
        }
        onSelected = ViewComposition {
            Box(modifier = Modifier.testTag("called-selected"))
        }
        view = DashboardViewCompositionCurrencyFork(onSelected, onMissing)
    }

    override fun tearDown() {
        called = false
    }

    @Test
    fun showsOnMissing_whenCurrencyNull() = inCompose(
        before = { viewModel.selectedCurrency.value = getInvalidCurrency() }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("called-missing").assertExists()
    }

    @Test
    fun showsOnSelected_whenCurrencyNotNull() = inCompose(
        before = { viewModel.selectedCurrency.value = getValidCurrency() }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("called-selected").assertExists()
    }

    private fun getValidCurrency(): CurrencyModel {
        return CurrencyModel(ExchangeRate(Currency.getInstance("USD"), 0.0, Date()))
    }

    private fun getInvalidCurrency(): CurrencyModel? {
        return null
    }

}