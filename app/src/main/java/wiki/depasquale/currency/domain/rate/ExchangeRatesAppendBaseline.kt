package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate
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
        return ExchangeRate(ExchangeRatesConstants.Baseline, 1.0, date)
    }

}