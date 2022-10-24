package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cursola.rate.view.ConvertedCurrency
import cursola.rate.view.ListingViewModel
import cursola.view.ExchangeRateItem
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.asFlagRes
import wiki.depasquale.currency.screen.favorite.plus
import java.util.Currency
import java.util.Locale

@Composable
fun ListingScreen(
    viewModel: ListingViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.items.collectAsState(initial = emptyList())
    ListingScreen(
        items = items,
        onAddItem = viewModel::add,
        onRemoveItem = viewModel::remove
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingScreen(
    items: List<ConvertedCurrency>,
    onAddItem: (Currency) -> Unit,
    onRemoveItem: (Currency) -> Unit,
    locale: Locale = Locale.getDefault()
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { padding ->
        LazyColumn(
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = ConvertedCurrency::currency) {
                ListingItem(
                    onAddItem = { onAddItem(it.currency) },
                    onRemoveItem = { onRemoveItem(it.currency) },
                    name = it.name(locale),
                    currency = it.currency,
                    isFavorite = it.isFavorite
                )
            }
        }
    }
}

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
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .then(modifier),
        icon = {
            if (isFavorite) ItemActiveIcon(onRemoveItem)
            else ItemInactiveIcon(onAddItem)
        },
        flag = {
            ListingItemFlag(resource = currency.asFlagRes())
        },
        text = {
            ListingItemText(name = name, currency = currency)
        }
    )
}

@Composable
fun ItemActiveIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(Color.Yellow)
        )
    }
}

@Composable
fun ItemInactiveIcon(
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

@Composable
private fun ListingItemFlag(
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
private fun ListingItemText(
    name: String,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(name)
        Text(
            currency.currencyCode,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .alpha(.5f)
        )
    }
}