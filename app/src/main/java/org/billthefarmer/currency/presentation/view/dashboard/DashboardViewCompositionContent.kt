package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import java.util.*
import kotlin.random.Random.Default.nextDouble

class DashboardViewCompositionContent : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        val currenciesState = model.currencies.collectAsState()
        Compose(
            currencies = currenciesState.value,
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

    @Composable
    private fun ExchangeRateItem(model: CurrencyModel, onCurrencyClick: (CurrencyModel) -> Unit) {
        Row(
            modifier = Modifier
                .testTag("content-exchange-rate-holder")
                .clickable { onCurrencyClick(model) }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val flagResource = model.getFlagResource(context)
            Image(
                modifier = Modifier
                    .width(122.dp)
                    .height(72.dp),
                painter = painterResource(id = flagResource),
                currency = model.rate.currency
            )
            Spacer(modifier = Modifier.width(32.dp))
            Labels(
                modifier = Modifier.weight(1f),
                currency = model.rate.currency,
                rate = model.rate.rate
            )
        }
    }

    @Composable
    private fun Image(modifier: Modifier, painter: Painter, currency: Currency) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Image(
                modifier = Modifier
                    .testTag("content-flag")
                    .fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.FillBounds,
                contentDescription = currency.displayName
            )
        }
    }

    @Composable
    private fun Labels(modifier: Modifier, currency: Currency, rate: Double) {
        Column(modifier = modifier) {
            val color = LocalContentColor.current
            Text(
                modifier = Modifier.testTag("content-currency-name"),
                text = currency.displayName,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Thin,
                overflow = TextOverflow.Ellipsis,
                color = color,
            )
            Text(
                modifier = Modifier.testTag("content-currency-value"),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Thin)) { append(currency.symbol) }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("%.2f".format(rate)) }
                },
                style = MaterialTheme.typography.h5,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
            )
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