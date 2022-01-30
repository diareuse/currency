package wiki.depasquale.currency.presentation.view.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wiki.depasquale.currency.domain.rate.ExchangeRates
import wiki.depasquale.currency.presentation.adapter.CurrencyModelAdapter
import wiki.depasquale.currency.presentation.view.dashboard.DashboardViewCompositionContentLoader
import wiki.depasquale.currency.screen.selection.SelectionViewModel
import wiki.depasquale.currency.tooling.Duplicates

@Duplicates(DashboardViewCompositionContentLoader::class)
class SelectionViewCompositionContentLoader(
    private val source: SelectionViewComposition,
    private val rates: ExchangeRates,
    private val adapter: CurrencyModelAdapter
) : SelectionViewComposition {

    @Composable
    override fun Compose(model: SelectionViewModel) {
        source.Compose(model = model)
        LaunchedEffect(model) {
            model.currencies.value = getCurrencies()
        }
    }

    private suspend fun getCurrencies() = withContext(Dispatchers.IO) {
        getExchangeRates().map(adapter::adapt)
    }

    private suspend fun getExchangeRates() = withContext(Dispatchers.IO) {
        rates.getCurrentRates()
    }

}