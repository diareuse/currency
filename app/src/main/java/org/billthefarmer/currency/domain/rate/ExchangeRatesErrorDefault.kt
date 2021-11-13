package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate

class ExchangeRatesErrorDefault(
    private val source: ExchangeRates,
    private val onError: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return try {
            source.getCurrentRates()
        } catch (e: Throwable) {
            onError.getCurrentRates()
        }
    }

}