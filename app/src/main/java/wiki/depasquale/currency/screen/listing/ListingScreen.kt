package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ListingScreen(
    items: List<ConvertedCurrency>,
    onAddItem: (Currency) -> Unit,
    onRemoveItem: (Currency) -> Unit,
    onNavigateBack: () -> Unit,
    locale: Locale = Locale.getDefault()
) {
    var filter by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = filter,
                        onValueChange = { filter = it },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text("Search here…")
                        }
                    )
                },
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
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items.filter { it.name(locale).contains(filter, ignoreCase = true) },
                key = ConvertedCurrency::currency
            ) {
                ListingItem(
                    modifier = Modifier.animateItemPlacement(),
                    onAddItem = { onAddItem(it.currency) },
                    onRemoveItem = { onRemoveItem(it.currency) },
                    name = it.name(locale),
                    currency = it.currency,
                    isFavorite = it.isFavorite
                )
            }
            item { Spacer(
                Modifier
                    .navigationBarsPadding()
                    .imePadding()) }
        }
    }
}