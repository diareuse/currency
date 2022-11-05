package wiki.depasquale.currency.screen.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cursola.rate.view.HistoricalViewModel
import cursola.view.topFadingEdge
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.copy
import wiki.depasquale.currency.screen.favorite.plus
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency

@Composable
fun HistoryScreen(
    viewModel: HistoricalViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    HistoryScreen(
        items = items,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HistoryScreen(
    items: List<HistoricalViewModel.HistoryValues>,
    onNavigateBack: () -> Unit,
    scrollState: ScrollState = rememberScrollState(),
) {
    LaunchedEffect(items) {
        if (scrollState.value == 0) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                title = {},
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

@Preview(showBackground = true)
@Composable
private fun Preview(
    items: List<HistoricalViewModel.HistoryValues> = Currency.getAvailableCurrencies().take(10)
        .map { HistoricalViewModel.HistoryValues(it, emptyList()) }
) {
    CurrencyTheme {
        HistoryScreen(
            items = items,
            onNavigateBack = {}
        )
    }
}