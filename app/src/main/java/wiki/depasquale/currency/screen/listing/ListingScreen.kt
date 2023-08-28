package wiki.depasquale.currency.screen.listing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import cursola.rate.view.ConvertedCurrency
import cursola.view.topFadingEdge
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.copy
import wiki.depasquale.currency.screen.favorite.plus
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListingScreen(
    items: List<ConvertedCurrency>,
    onAddItem: (Currency) -> Unit,
    onRemoveItem: (Currency) -> Unit,
    onNavigateBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault()
) {
    var filter by remember { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    ListingSearchField(
                        filter = filter,
                        onFilterChanged = { filter = it }
                    )
                },
                navigationIcon = {
                    if (onNavigateBack != null) IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .topFadingEdge(48.dp, state),
            contentPadding = padding.copy(top = 0.dp) + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state
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
            item {
                Spacer(
                    Modifier
                        .navigationBarsPadding()
                        .imePadding()
                )
            }
        }
    }
}