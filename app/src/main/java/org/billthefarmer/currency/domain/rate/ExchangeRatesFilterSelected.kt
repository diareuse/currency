package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.preference.ExchangeRatePreferenceReader

class ExchangeRatesFilterSelected(
    private val source: ExchangeRates,
    private val preferences: ExchangeRatePreferenceReader
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val preferences = preferences.read()
        return source.getCurrentRates().filter { it.currency in preferences.selectedCurrencies }
    }

}