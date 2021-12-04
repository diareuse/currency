package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.presentation.view.common.ExchangeRateItem
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import java.util.*
import kotlin.random.Random.Default.nextDouble

class DashboardViewCompositionContent : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        val currenciesState by model.currencies.collectAsState()
        val selected by model.selectedCurrency.collectAsState()
        Compose(
            currencies = currenciesState.filter { it != selected },
            onCurrencyClick = { model.selectedCurrency.value = it }
        )
    }

    @Composable
    private fun Compose(currencies: List<CurrencyModel>, onCurrencyClick: (CurrencyModel) -> Unit) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(32.dp)
        ) {
            items(currencies) {
                ExchangeRateItem(it, onCurrencyClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        fun getRate(currency: String) = ExchangeRate(
            currency = Currency.getInstance(currency),
            rate = nextDouble(0.0, 30000.0),
            time = Date()
        )

        val currencies = listOf(
            CurrencyModel(getRate("USD")),
            CurrencyModel(getRate("PHP")),
            CurrencyModel(getRate("EUR")),
            CurrencyModel(getRate("CZK")),
        )

        Compose(currencies) {}
    }

}