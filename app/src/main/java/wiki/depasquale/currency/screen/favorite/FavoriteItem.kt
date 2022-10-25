package wiki.depasquale.currency.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cursola.view.CurrencyFlag
import cursola.view.CurrencyValue
import cursola.view.ExchangeRateItem
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import java.util.Locale

@Composable
fun FavoriteItem(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    currency: Currency,
) {
    ExchangeRateItem(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .then(modifier),
        flag = {
            FavoriteItemFlag(currency = currency)
        },
        text = {
            FavoriteItemText(name = name, value = value, currency = currency)
        }
    )
}

@Composable
private fun FavoriteItemFlag(
    currency: Currency
) {
    CurrencyFlag(
        modifier = Modifier
            .width(40.dp)
            .height(30.dp)
            .shadow(8.dp)
            .clip(MaterialTheme.shapes.small),
        currency = currency
    )
}

@Composable
private fun FavoriteItemText(
    name: String,
    value: String,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                currency.currencyCode,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alpha(.5f)
            )
        }
        CurrencyValue(value, currency.getSymbol(Locale.getDefault()))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CurrencyTheme {
        FavoriteItem(
            name = "US Dollar",
            value = "$12.30",
            currency = Currency.getInstance("USD")
        )
    }
}