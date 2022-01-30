package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate

class ExchangeRatesSort(
    private val source: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return source.getCurrentRates().sortedBy { it.currency.displayName }
    }

}