package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate
import java.util.*

class ExchangeRatesAppendBaseline(
    private val source: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return source.getCurrentRates() + getBaseline()
    }

    private fun getBaseline(): ExchangeRate {
        return ExchangeRate(Currency.getInstance("EUR"), 1.0, Date())
    }

}