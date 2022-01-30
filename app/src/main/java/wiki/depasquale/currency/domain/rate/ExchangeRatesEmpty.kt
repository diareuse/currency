package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate

class ExchangeRatesEmpty : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return emptyList()
    }

}