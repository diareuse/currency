package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel

class DashboardViewCompositionCurrencyFork(
    private val onSelected: DashboardViewComposition,
    private val onMissing: DashboardViewComposition
) : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        val currencyState = model.selectedCurrency.collectAsState()
        val currency = currencyState.value

        if (currency == null) {
            onMissing.Compose(model = model)
        } else {
            onSelected.Compose(model = model)
        }
    }

}