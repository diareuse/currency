package org.billthefarmer.currency.presentation.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.domain.rate.RateCalculator
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.screen.style.ShapeMedium
import java.util.*

@Composable
fun ExchangeRateItem(
    model: CurrencyModel,
    modifier: Modifier = Modifier,
    calculator: RateCalculator = ExchangeRateItemDefaults.Calculator,
    onCurrencyClick: () -> Unit
) {
    Row(
        modifier = modifier
            .testTag("content-exchange-rate-holder")
            .clickable(onClick = onCurrencyClick)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val flagResource = remember(model) {
            model.getFlagResource(context)
        }
        Image(
            modifier = Modifier
                .width(95.dp)
                .height(56.dp),
            painter = painterResource(id = flagResource),
            currency = model.rate.currency
        )
        Spacer(modifier = Modifier.width(32.dp))
        Labels(
            modifier = Modifier.weight(1f),
            currency = model.rate.currency,
            rate = remember(calculator, model) {
                calculator.getAdjustedRate(model.rate.rate)
            }
        )
    }
}

@Composable
private fun Image(modifier: Modifier, painter: Painter, currency: Currency) {
    Surface(
        modifier = modifier,
        shape = ShapeMedium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        Image(
            modifier = Modifier
                .testTag("content-flag")
                .semantics { contentDescription = currency.displayName }
                .fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.Crop,
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
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Thin,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color,
        )
        Text(
            modifier = Modifier.testTag("content-currency-value"),
            text = remember(currency, rate) {
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("%.2f".format(rate)) }
                    withStyle(SpanStyle(fontWeight = FontWeight.Thin)) { append(currency.symbol) }
                }
            },
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
        )
    }
}