package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate

class ExchangeRatesEmptyFork(
    private val source: ExchangeRates,
    private val otherwise: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        var output = source.getCurrentRates()
        if (output.isEmpty())
            output = otherwise.getCurrentRates()
        return output
    }

}