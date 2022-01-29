package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.rate.ExchangeRates
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapter
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel

class DashboardViewCompositionContentLoader(
    private val source: DashboardViewComposition,
    private val rates: ExchangeRates,
    private val adapter: CurrencyModelAdapter
) : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        source.Compose(model = model)
        LaunchedEffect(model) {
            model.currencies.value = getCurrencies()
        }
    }

    private suspend fun getCurrencies(): List<CurrencyModel> {
        return getExchangeRates().map(adapter::adapt)
    }

    private suspend fun getExchangeRates(): List<ExchangeRate> {
        return withContext(Dispatchers.IO) {
            rates.getCurrentRates()
        }
    }

}