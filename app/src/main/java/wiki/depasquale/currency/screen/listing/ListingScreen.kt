package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cursola.rate.view.ConvertedCurrency
import cursola.rate.view.ListingViewModel
import wiki.depasquale.currency.R
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
        onRemoveItem = viewModel::remove,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingScreen(
    items: List<ConvertedCurrency>,
    onAddItem: (Currency) -> Unit,
    onRemoveItem: (Currency) -> Unit,
    onNavigateBack: () -> Unit,
    locale: Locale = Locale.getDefault()
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Pick your currency") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
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