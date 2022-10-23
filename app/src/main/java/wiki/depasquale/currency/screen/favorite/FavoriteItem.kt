package wiki.depasquale.currency.screen.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cursola.view.CurrencyValue
import cursola.view.ExchangeRateItem
import wiki.depasquale.currency.R
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
            FavoriteItemFlag(resource = currency.asFlagRes())
        },
        text = {
            FavoriteItemText(name = name, value = value, currency = currency)
        }
    )
}

@Composable
private fun FavoriteItemFlag(
    resource: Int
) {
    Image(
        modifier = Modifier
            .width(42.dp)
            .height(25.dp)
            .clip(MaterialTheme.shapes.small)
            .shadow(8.dp),
        painter = painterResource(id = resource),
        contentDescription = null,
        contentScale = ContentScale.Crop
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(name)
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

@SuppressLint("DiscouragedApi")
@Composable
fun Currency.asFlagRes(): Int {
    val context = LocalContext.current
    val resources = context.resources ?: return R.drawable.flag__unknown
    val name = "flag_${currencyCode.lowercase()}"
    val id = resources.getIdentifier(name, "drawable", context.packageName)
    if (id == 0)
        return R.drawable.flag__unknown
    return id
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