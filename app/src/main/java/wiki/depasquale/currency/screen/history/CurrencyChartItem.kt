package wiki.depasquale.currency.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import cursola.view.ChartDouble
import cursola.view.CurrencyFlag
import cursola.view.ExchangeRateItemInline
import java.util.Currency

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