package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.WindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import wiki.depasquale.currency.R
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.rate.ExchangeRatesConstants
import wiki.depasquale.currency.domain.rate.RateCalculator
import wiki.depasquale.currency.domain.rate.RateCalculatorPivot
import wiki.depasquale.currency.presentation.gesture.swipeable
import wiki.depasquale.currency.presentation.model.CurrencyModel
import wiki.depasquale.currency.presentation.view.common.ExchangeRateItem
import wiki.depasquale.currency.presentation.view.common.ExchangeRateItemDefaults
import wiki.depasquale.currency.presentation.view.main.LocalNavHostController
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.screen.style.ShapeMedium
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random.Default.nextDouble

class DashboardViewCompositionContent : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        val currenciesState by model.currencies.collectAsState()
        val selected by model.selectedCurrency.collectAsState()
        val value by model.amountDouble.collectAsState()
        val calculator = remember(selected, value) {
            RateCalculatorPivot(selected?.rate, value)
        }
        val navigator = LocalNavHostController.current
        Compose(
            currencies = remember(
                currenciesState,
                selected
            ) { currenciesState.filter { it != selected } },
            calculator = calculator,
            onCurrencyClick = { model.selectedCurrency.value = it },
            onDeleteClick = { model.addPendingDeletion(it) },
            onDetailClick = { navigator.navigate("detail/${it.rate.currency.currencyCode}") },
        )
    }

    @Composable
    private fun Compose(
        currencies: List<CurrencyModel>,
        calculator: RateCalculator,
        onCurrencyClick: (CurrencyModel) -> Unit,
        onDeleteClick: (CurrencyModel) -> Unit,
        onDetailClick: (CurrencyModel) -> Unit,
    ) {
        val insets = LocalWindowInsets.current
        val padding = rememberInsetsPaddingValues(
            if (insets.ime.isVisible) insets.statusBars
            else WindowInsets.Type.Empty,
            additionalStart = 32.dp,
            additionalTop = 32.dp,
            additionalBottom = 32.dp,
            additionalEnd = 32.dp
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            items(currencies, key = { it.rate.currency.currencyCode }) {
                ListItem(
                    item = it,
                    calculator = calculator,
                    onCurrencyClick = { onCurrencyClick(it) },
                    onDeleteClick = { onDeleteClick(it) },
                    onDetailClick = { onDetailClick(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    private fun ListItem(
        item: CurrencyModel,
        calculator: RateCalculator,
        onCurrencyClick: () -> Unit,
        onDeleteClick: () -> Unit,
        onDetailClick: () -> Unit
    ) {
        Box {
            var actionsSize by remember { mutableStateOf(0) }
            var offset by remember { mutableStateOf(0f) }
            val offsetAnimated by animateFloatAsState(targetValue = offset)

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged { actionsSize = it.width }
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            role = Role.Button,
                            enabled = item.rate.currency != ExchangeRatesConstants.Baseline
                        ) {
                            offset = 0f
                            onDetailClick()
                        }
                        .background(MaterialTheme.colorScheme.secondary, ShapeMedium)
                        .size(48.dp)
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = "Detail",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    modifier = Modifier
                        .clickable(role = Role.Button) {
                            offset = 0f
                            onDeleteClick()
                        }
                        .background(MaterialTheme.colorScheme.error, ShapeMedium)
                        .size(48.dp)
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }

            DisposableEffect(Unit) {
                offset = 0f
                onDispose {}
            }

            ExchangeRateItem(
                modifier = Modifier
                    .offset { IntOffset(offsetAnimated.roundToInt(), 0) }
                    .swipeable(
                        getActionsSize = { actionsSize },
                        getOffset = { offset },
                        onOffsetChanged = { offset = it }
                    )
                    .background(MaterialTheme.colorScheme.surface),
                model = item,
                calculator = calculator,
                onCurrencyClick = onCurrencyClick
            )
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        fun getRate(currency: String) = ExchangeRate(
            currency = Currency.getInstance(currency),
            rate = nextDouble(0.0, 30000.0),
            time = Date()
        )

        val currencies = listOf(
            CurrencyModel(getRate("USD")),
            CurrencyModel(getRate("PHP")),
            CurrencyModel(getRate("EUR")),
            CurrencyModel(getRate("CZK")),
        )

        Compose(currencies, ExchangeRateItemDefaults.Calculator, {}, {}, {})
    }

}