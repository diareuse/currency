package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.preference.ExchangeRatePreferenceReader

class ExchangeRatesFilterSelected(
    private val source: ExchangeRates,
    private val preferences: ExchangeRatePreferenceReader
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val preferences = preferences.read()
        val selectedCurrencies = preferences.selectedCurrencies
        return source.getCurrentRates().filter { it.currency in selectedCurrencies }
    }

}