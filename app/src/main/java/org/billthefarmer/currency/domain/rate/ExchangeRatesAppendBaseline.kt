package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.model.ExchangeRate
import java.util.*

class ExchangeRatesAppendBaseline(
    private val source: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val rates = source.getCurrentRates()
        val date = rates.firstOrNull()?.time ?: Date()
        return rates + getBaseline(date)
    }

    private fun getBaseline(date: Date): ExchangeRate {
        return ExchangeRate(Currency.getInstance("EUR"), 1.0, date)
    }

}