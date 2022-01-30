package wiki.depasquale.currency.presentation.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import wiki.depasquale.currency.presentation.view.LocalSnackbarController
import wiki.depasquale.currency.presentation.view.common.ChartDouble
import wiki.depasquale.currency.presentation.view.common.vertical
import wiki.depasquale.currency.presentation.view.show
import wiki.depasquale.currency.screen.detail.DetailViewModel
import java.text.DateFormat

class DetailViewCompositionContent : DetailViewComposition {

    @Composable
    override fun Compose(model: DetailViewModel) {
        val rates by model.rates.collectAsState()
        val state = rememberScrollState()
        val samples = remember(rates) { rates.map { it.value } }
        val formatter = remember {
            DateFormat.getDateInstance(DateFormat.MEDIUM)
        }
        LaunchedEffect(rates) {
            if (state.value == 0) {
                state.scrollTo(state.maxValue)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(state)
        ) {
            val density = LocalDensity.current
            val sampleWidth = 16.dp
            val width = sampleWidth * samples.size
            val snackbar = LocalSnackbarController.current
            Row(
                modifier = Modifier
                    .width(width)
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    modifier = Modifier.vertical(-90f),
                    text = rates.firstOrNull()?.date?.let { formatter.format(it) }.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier.vertical(-90f),
                    text = rates.lastOrNull()?.date?.let { formatter.format(it) }.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            ChartDouble(
                modifier = Modifier
                    .width(width)
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            val sampleWidthPx = with(density) { sampleWidth.toPx() }
                            val index = (it.x / sampleWidthPx)
                                .toInt()
                                .coerceIn(rates.indices)
                            val value = rates[index]
                            snackbar.show(buildAnnotatedString {
                                append(formatter.format(value.date))
                                append('\n')
                                append("Value equates to ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                                    append(value.value.toString())
                                }
                                append(model.currency.symbol)
                                append(" for 1€")
                            })
                        }
                    },
                samples = samples,
                background = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(.8f)
                    )
                )
            )
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(.8f))
                    .navigationBarsPadding()
                    .padding(vertical = 16.dp)
                    .width(width)
            ) {
                for ((index, sample) in samples.withIndex()) {
                    if (index % 2 != 0) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .vertical(-90f)
                                .align(Alignment.Bottom),
                            text = sample.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
            }
        }
    }

}