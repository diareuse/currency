package wiki.depasquale.currency.screen.history

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import cursola.rate.view.HistoricalViewModel
import cursola.view.topFadingEdge
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.copy
import wiki.depasquale.currency.screen.favorite.plus
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import java.util.Date
import kotlin.random.Random.Default.nextDouble

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    items: List<HistoricalViewModel.HistoryValues>,
    onNavigateBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    LaunchedEffect(items) {
        if (scrollState.value == 0) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                title = { Text(stringResource(id = R.string.history_title)) },
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
        var isShowMetadata by remember { mutableStateOf(false) }
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
            items(items.filterNot { it.currency.currencyCode == "EUR" }, { it.currency }) {
                CurrencyChartItem(
                    modifier = Modifier.animateItemPlacement(),
                    currency = it.currency,
                    values = it.values.map(HistoricalViewModel.ChartValue::y).asReversed(),
                    state = scrollState,
                    showMetadata = isShowMetadata,
                    onShowMetadata = { isShowMetadata = it }
                )
            }
            item {
                Text(
                    stringResource(id = R.string.history_footnote),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Spacer(
                    Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun Preview(
    items: List<HistoricalViewModel.HistoryValues> = Currency.getAvailableCurrencies().take(10)
        .map {
            HistoricalViewModel.HistoryValues(
                it,
                List(5) { HistoricalViewModel.ChartValue(Date(), nextDouble()) })
        }
) {
    CurrencyTheme {
        HistoryScreen(
            items = items,
            onNavigateBack = {}
        )
    }
}