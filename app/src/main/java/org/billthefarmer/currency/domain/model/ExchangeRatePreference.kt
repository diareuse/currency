package org.billthefarmer.currency.domain.model

import org.billthefarmer.currency.domain.adapter.CurrencyPreferenceAdapter
import org.billthefarmer.currency.domain.adapter.CurrencyValueProvider
import org.billthefarmer.currency.domain.preference.PreferenceKey
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
