package org.billthefarmer.currency.presentation.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import java.util.*

@Composable
fun ExchangeRateItem(
    model: CurrencyModel,
    calculator: RateCalculator = ExchangeRateItemDefaults.Calculator,
    onCurrencyClick: (CurrencyModel) -> Unit
) {
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
                .width(95.dp)
                .height(56.dp),
            painter = painterResource(id = flagResource),
            currency = model.rate.currency
        )
        Spacer(modifier = Modifier.width(32.dp))
        Labels(
            modifier = Modifier.weight(1f),
            currency = model.rate.currency,
            rate = calculator.getAdjustedRate(model.rate.rate)
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
                .semantics { contentDescription = currency.displayName }
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