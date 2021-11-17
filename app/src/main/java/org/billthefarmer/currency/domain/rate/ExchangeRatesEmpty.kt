package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate

class ExchangeRatesEmpty : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return emptyList()
    }

}