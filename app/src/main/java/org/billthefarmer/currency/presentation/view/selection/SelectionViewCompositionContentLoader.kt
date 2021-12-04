package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.rate.ExchangeRates
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapter
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.presentation.view.dashboard.DashboardViewCompositionContentLoader
import org.billthefarmer.currency.screen.selection.SelectionViewModel
import org.billthefarmer.currency.tooling.Duplicates

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

    private suspend fun getCurrencies(): List<CurrencyModel> {
        return getExchangeRates().map(adapter::adapt)
    }

    private suspend fun getExchangeRates(): List<ExchangeRate> {
        return withContext(Dispatchers.Default) {
            rates.getCurrentRates()
        }
    }

}