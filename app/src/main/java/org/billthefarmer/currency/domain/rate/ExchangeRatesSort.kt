package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate

class ExchangeRatesSort(
    private val source: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return source.getCurrentRates().sortedBy { it.currency.displayName }
    }

}