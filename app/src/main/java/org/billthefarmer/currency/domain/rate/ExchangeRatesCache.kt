package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate

class ExchangeRatesCache(
    private val source: ExchangeRates
) : ExchangeRates {

    @Volatile
    private var rates = emptyList<ExchangeRate>()

    override fun getCurrentRates(): List<ExchangeRate> {
        if (rates.isNotEmpty()) {
            return rates
        }
        return source.getCurrentRates().also {
            rates = it
        }
    }

}