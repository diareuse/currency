package wiki.depasquale.currency.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cursola.rate.view.HistoricalViewModel
import cursola.view.ChartDouble
import cursola.view.CurrencyFlag
import wiki.depasquale.currency.R
import wiki.depasquale.currency.screen.favorite.plus
import java.util.Currency

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoricalViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val scrollState = rememberScrollState()
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    "Charts here are showing a 3-month history of any favorite currency in relation to Euro.",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyChartItem(
    currency: Currency,
    values: List<Double>,
    state: ScrollState,
    showMetadata: Boolean,
    onShowMetadata: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        ExchangeRateItemInline(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium)
                .combinedClickable(onLongClick = { onShowMetadata(!showMetadata) }) {
                    isExpanded = !isExpanded
                },
            flag = {
                CurrencyFlag(
                    modifier = Modifier
                        .width(40.dp)
                        .height(32.dp)
                        .shadow(8.dp)
                        .clip(MaterialTheme.shapes.small),
                    currency = currency
                )
            },
            content = {
                if (values.isEmpty()) LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .alpha(.3f)
                )
                Box(modifier = Modifier.horizontalScroll(state)) {
                    ChartDouble(
                        modifier = Modifier
                            .width((values.size * 16).dp)
                            .height(if (isExpanded) 112.dp else 56.dp),
                        samples = values,
                        background = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                }
            }
        )
        AnimatedVisibility(visible = showMetadata) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(.4f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_min),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Text("%.2f".format(values.minByOrNull { it } ?: 0.0),
                    style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_max),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Text("%.2f".format(values.maxByOrNull { it } ?: 0.0),
                    style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ExchangeRateItemInline(
    flag: @Composable () -> Unit,
    content: @Composable (flagSize: IntSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        var flagSize by remember { mutableStateOf(IntSize.Zero) }
        content(flagSize)
        Box(
            modifier = Modifier
                .padding(12.dp)
                .onSizeChanged { flagSize = it }
        ) {
            flag()
        }
    }
}