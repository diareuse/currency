package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.rate.ExchangeRates
import wiki.depasquale.currency.presentation.adapter.CurrencyModelAdapter
import wiki.depasquale.currency.presentation.model.CurrencyModel
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel

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