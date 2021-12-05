package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.presentation.view.common.ExchangeRateItem
import org.billthefarmer.currency.presentation.view.dashboard.DashboardViewCompositionContent
import org.billthefarmer.currency.screen.selection.SelectionViewModel
import org.billthefarmer.currency.tooling.Duplicates

@Duplicates(DashboardViewCompositionContent::class)
class SelectionViewCompositionContent : SelectionViewComposition {

    private val contentPadding: PaddingValues
        @Composable get() {
            val insets = LocalWindowInsets.current
            val insetsNav = insets.navigationBars.animatedInsets.bottom
            val insetsNavDp = with(LocalDensity.current) {
                insetsNav.toDp()
            }
            return PaddingValues(
                start = 32.dp,
                end = 32.dp,
                top = 32.dp,
                bottom = 32.dp + insetsNavDp
            )
        }

    @Composable
    override fun Compose(model: SelectionViewModel) {
        val items by model.currencies.collectAsState()
        Content(
            currencies = items,
            padding = contentPadding,
            onCurrencyClick = { model.addPending(it) }
        )
    }

    @Composable
    private fun Content(
        currencies: List<CurrencyModel>,
        padding: PaddingValues,
        onCurrencyClick: (CurrencyModel) -> Unit
    ) {
        LazyColumn(contentPadding = padding) {
            items(currencies) {
                ExchangeRateItem(it, onCurrencyClick = { onCurrencyClick(it) })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}