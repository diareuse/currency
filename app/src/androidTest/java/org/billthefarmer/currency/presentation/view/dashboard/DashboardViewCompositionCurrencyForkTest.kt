package org.billthefarmer.currency.presentation.view.dashboard

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.util.*

class DashboardViewCompositionCurrencyForkTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionCurrencyFork

    @Mock
    lateinit var onSelected: DashboardViewComposition

    @Mock
    lateinit var onMissing: DashboardViewComposition

    private var called = false

    override fun prepare() {
        MockitoAnnotations.openMocks(this).close()
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionCurrencyFork(
            onSelected,
            onMissing
        )
    }

    override fun tearDown() {
        called = false
    }

    @Test
    fun showsOnMissing_whenCurrencyNull() = inCompose {
        whenever(onMissing.Compose(model = viewModel)).then { called = true;Unit }

        viewModel.selectedCurrency.value = getInvalidCurrency()
        view.Compose(model = viewModel)

        assertThat(called).isTrue()
    } asserts {}

    @Test
    fun showsOnSelected_whenCurrencyNotNull() = inCompose {
        whenever(onSelected.Compose(model = viewModel)).then { called = true;Unit }

        viewModel.selectedCurrency.value = getValidCurrency()
        view.Compose(model = viewModel)

        assertThat(called).isTrue()
    } asserts {}

    private fun getValidCurrency(): CurrencyModel {
        return CurrencyModel(ExchangeRate(Currency.getInstance("USD"), 0.0, Date()))
    }

    private fun getInvalidCurrency(): CurrencyModel? {
        return null
    }

}