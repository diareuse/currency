package wiki.depasquale.currency.screen.favorite

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import cursola.rate.view.ConvertedCurrency
import cursola.view.CurrencyFlag
import cursola.view.topFadingEdge
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import java.util.Locale

@Composable
fun FavoriteScreen(
    items: List<ConvertedCurrency>,
    selected: Currency,
    value: String,
    onNavigateToListing: (() -> Unit)?,
    onNavigateToHistory: (() -> Unit)?,
    onValueChanged: (String) -> Unit,
    onItemSelected: (ConvertedCurrency) -> Unit,
    modifier: Modifier = Modifier,
    manager: FocusManager = LocalFocusManager.current,
) {
    FavoriteScreen(
        modifier = modifier,
        items = items,
        value = value,
        selected = selected,
        onValueChanged = onValueChanged,
        onItemSelected = onItemSelected,
        onCloseKeyboard = manager::clearFocus,
        onNavigateToListing = onNavigateToListing,
        onNavigateToHistory = onNavigateToHistory
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteScreen(
    items: List<ConvertedCurrency>,
    value: String,
    selected: Currency,
    onValueChanged: (String) -> Unit,
    onItemSelected: (ConvertedCurrency) -> Unit,
    onCloseKeyboard: () -> Unit,
    onNavigateToListing: (() -> Unit)?,
    onNavigateToHistory: (() -> Unit)?,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.getDefault()
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        modifier = modifier,
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
            CurrencyFlag(
                modifier = Modifier
                    .padding(48.dp)
                    .size(200.dp, 150.dp),
                currency = selected
            )
        }
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .topFadingEdge(48.dp, state),
            contentPadding = padding.copy(top = 0.dp) + PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state
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
            if (onNavigateToHistory != null) item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onNavigateToHistory
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = "Show history",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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

@Composable
fun PaddingValues.copy(
    start: Dp? = null,
    top: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null
): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = start ?: calculateStartPadding(direction),
        top = top ?: calculateTopPadding(),
        end = end ?: calculateEndPadding(direction),
        bottom = bottom ?: calculateBottomPadding()
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
            value = "1.0",
            selected = Currency.getInstance("EUR"),
            onValueChanged = {},
            onCloseKeyboard = {},
            onNavigateToListing = {},
            onNavigateToHistory = {}
        )
    }
}