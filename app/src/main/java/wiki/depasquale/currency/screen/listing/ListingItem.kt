package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cursola.view.CurrencyFlag
import cursola.view.ExchangeRateItem
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency

@Composable
fun ListingItem(
    modifier: Modifier = Modifier,
    onAddItem: () -> Unit,
    onRemoveItem: () -> Unit,
    name: String,
    currency: Currency,
    isFavorite: Boolean
) {
    ExchangeRateItem(
        background = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        icon = {
            if (isFavorite) ItemActiveIcon(onRemoveItem)
            else ItemInactiveIcon(onAddItem)
        },
        flag = {
            ListingItemFlag(currency = currency)
        },
        text = {
            ListingItemText(name = name, currency = currency)
        }
    )
}

@Composable
private fun ListingItemFlag(
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
private fun ListingItemText(
    name: String,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            currency.currencyCode,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .alpha(.5f)
        )
    }
}

@Composable
private fun ItemActiveIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun ItemInactiveIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_favorite_not),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CurrencyTheme {
        ListingItem(
            onAddItem = { /*TODO*/ },
            onRemoveItem = { /*TODO*/ },
            name = "US Dollar",
            currency = Currency.getInstance("USD"),
            isFavorite = true
        )
    }
}