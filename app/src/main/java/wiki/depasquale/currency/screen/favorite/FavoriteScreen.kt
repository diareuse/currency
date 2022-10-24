package wiki.depasquale.currency.screen.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cursola.rate.view.ConvertedCurrency
import cursola.rate.view.FavoritesViewModel
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import java.util.Locale

@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel,
    onNavigateToListing: () -> Unit
) {
    val items by viewModel.items.collectAsState(initial = emptyList())
    val selected by viewModel.selected.collectAsState()
    val value by viewModel.value.collectAsState()
    val manager = LocalFocusManager.current
    FavoriteScreen(
        items = items,
        value = value,
        selected = selected,
        onValueChanged = { viewModel.value.value = it },
        onItemSelected = { viewModel.selected.value = it.currency },
        onCloseKeyboard = manager::clearFocus,
        onNavigateToListing = onNavigateToListing
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteScreen(
    items: List<ConvertedCurrency>,
    value: Double,
    selected: Currency,
    onValueChanged: (Double) -> Unit,
    onItemSelected: (ConvertedCurrency) -> Unit,
    onCloseKeyboard: () -> Unit,
    onNavigateToListing: () -> Unit,
    locale: Locale = Locale.getDefault()
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            FavoriteInputField(
                modifier = Modifier
                    .statusBarsPadding()
                    .focusRequester(focusRequester),
                value = value,
                selected = selected,
                locale = locale,
                onValueChanged = onValueChanged,
                onCloseKeyboard = onCloseKeyboard
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp)
                .alpha(.4f),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                modifier = Modifier
                    .padding(48.dp)
                    .size(200.dp, 133.dp),
                painter = painterResource(id = selected.asFlagRes()),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        LazyColumn(
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FavoriteHeadline(onAddClick = onNavigateToListing)
            }
            items(items = items, key = { it.currency }) { currency ->
                FavoriteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement()
                        .clickable { onItemSelected(currency) },
                    name = currency.name(locale),
                    value = currency.toString(locale),
                    currency = currency.currency
                )
            }
            item {
                Spacer(modifier = Modifier.imePadding())
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(direction) + other.calculateStartPadding(direction),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = calculateBottomPadding() + other.calculateBottomPadding()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(ConvertedCurrenciesProvider::class)
    items: List<ConvertedCurrency>
) {
    CurrencyTheme {
        FavoriteScreen(
            items = items,
            onItemSelected = {},
            value = 1.0,
            selected = Currency.getInstance("EUR"),
            onValueChanged = {},
            onCloseKeyboard = {},
            onNavigateToListing = {}
        )
    }
}