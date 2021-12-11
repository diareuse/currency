package org.billthefarmer.currency.presentation.view.detail

import androidx.compose.runtime.Composable
import org.billthefarmer.currency.domain.rate.ExchangeRates
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapter
import org.billthefarmer.currency.screen.detail.DetailViewModel

class DetailViewCompositionContentLoader(
    private val source: DetailViewComposition,
    private val rates: ExchangeRates,
    private val adapter: CurrencyModelAdapter
) : DetailViewComposition {

    @Composable
    override fun Compose(model: DetailViewModel) {
        source.Compose(model = model)
    }

}