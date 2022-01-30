package wiki.depasquale.currency.domain.model

import wiki.depasquale.currency.domain.adapter.CurrencyPreferenceAdapter
import wiki.depasquale.currency.domain.adapter.CurrencyValueProvider
import wiki.depasquale.currency.domain.preference.PreferenceKey
import java.util.*

data class ExchangeRatePreference(
    @PreferenceKey(
        name = "selected",
        adapter = CurrencyPreferenceAdapter::class,
        defaultValueProvider = CurrencyValueProvider::class
    )
    @get:PreferenceKey(
        name = "selected",
        adapter = CurrencyPreferenceAdapter::class
    )
    val selectedCurrencies: Collection<Currency>
)
