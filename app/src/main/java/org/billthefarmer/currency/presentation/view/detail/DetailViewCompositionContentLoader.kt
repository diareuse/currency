package org.billthefarmer.currency.presentation.view.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.rate.ExchangeRates
import org.billthefarmer.currency.presentation.adapter.DaySnapshotAdapter
import org.billthefarmer.currency.presentation.model.DaySnapshot
import org.billthefarmer.currency.screen.detail.DetailViewModel

class DetailViewCompositionContentLoader(
    private val source: DetailViewComposition,
    private val rates: ExchangeRates,
    private val adapter: DaySnapshotAdapter
) : DetailViewComposition {

    @Composable
    override fun Compose(model: DetailViewModel) {
        source.Compose(model = model)
        LaunchedEffect(model) {
            model.rates.value = getRates()
        }
    }

    private suspend fun getRates(): List<DaySnapshot> {
        return getExchangeRates().map(adapter::adapt)
    }

    private suspend fun getExchangeRates(): List<ExchangeRate> {
        return withContext(Dispatchers.Default) {
            rates.getCurrentRates()
        }
    }

}