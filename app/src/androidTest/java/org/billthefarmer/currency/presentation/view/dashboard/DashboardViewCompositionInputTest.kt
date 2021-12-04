package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel
import org.billthefarmer.currency.tooling.ComposeTest
import org.junit.Test
import java.util.*
import kotlin.random.Random.Default.nextDouble

class DashboardViewCompositionInputTest : ComposeTest() {

    private lateinit var view: DashboardViewCompositionInput
    private lateinit var viewModel: DashboardViewModel

    override fun prepare() {
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionInput()
    }

    @Test
    fun hasFlag() = inCompose(
        before = { viewModel.selectedCurrency.value = CurrencyModel(getExchangeRate()) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithContentDescription("flag").assertExists()
        onNodeWithTag("input-flag").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun hasTextInput() = inCompose(
        before = { viewModel.selectedCurrency.value = CurrencyModel(getExchangeRate()) }
    ) {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("input-text").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun isHidden_whenSelectedCurrencyNull() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("input-text").assertDoesNotExist()
        onNodeWithTag("input-flag").assertDoesNotExist()
    }

    // ---

    private fun getExchangeRate(): ExchangeRate {
        return ExchangeRate(Currency.getInstance("USD"), nextDouble(), Date())
    }

}