package wiki.depasquale.currency.screen.history

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import cursola.view.ChartDouble
import cursola.view.CurrencyFlag
import cursola.view.ExchangeRateItemInline
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import kotlin.random.Random.Default.nextDouble

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
        var isExpanded by rememberSaveable(currency.currencyCode) { mutableStateOf(false) }
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
                ) else BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val maxWidth = maxWidth
                    val height by animateDpAsState(targetValue = if (isExpanded) 112.dp else 56.dp)
                    val scale by animateFloatAsState(targetValue = if (isExpanded) 2f else 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(state)
                            .height(height),
                        contentAlignment = Alignment.Center
                    ) {
                        val width = max((values.size * 16).dp, maxWidth)
                        val widthPx = with(LocalDensity.current) { width.toPx() }
                        ChartDouble(
                            modifier = Modifier
                                .width(width)
                                .heightIn(min = 56.dp, max = 112.dp)
                                .scale(scaleX = 1f, scaleY = scale),
                            samples = values,
                            background = Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            initialSize = Rect(Offset.Zero, Size(widthPx, 0f))
                        )
                    }
                }
            }
        )
        AnimatedVisibility(visible = showMetadata) {
            ChartMetadataItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(.4f),
                max = values.minByOrNull { it },
                min = values.maxByOrNull { it },
                currency = currency
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview() {
    CurrencyTheme {
        CurrencyChartItem(
            currency = Currency.getInstance("CZK"),
            values = List(5) { nextDouble() },
            state = rememberScrollState(),
            showMetadata = false,
            onShowMetadata = {}
        )
    }
}