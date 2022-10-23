package wiki.depasquale.currency.screen.favorite

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import cursola.rate.view.ConvertedCurrency
import java.util.Currency

class ConvertedCurrencyProvider : CollectionPreviewParameterProvider<ConvertedCurrency>(
    listOf(
        ConvertedCurrency(
            Currency.getInstance("CZK"),
            23.4,
            true
        ),
        ConvertedCurrency(
            Currency.getInstance("USD"),
            1.04,
            true
        )
    )
)