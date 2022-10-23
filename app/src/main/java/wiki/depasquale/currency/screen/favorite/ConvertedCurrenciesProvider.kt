package wiki.depasquale.currency.screen.favorite

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import cursola.rate.view.ConvertedCurrency

class ConvertedCurrenciesProvider : CollectionPreviewParameterProvider<List<ConvertedCurrency>>(
    listOf(ConvertedCurrencyProvider().values.toList())
)