package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.model.ExchangeRate

class ExchangeRatesErrorDefault(
    private val source: ExchangeRates,
    private val onError: ExchangeRates
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return try {
            source.getCurrentRates()
        } catch (e: Throwable) {
            e.printStackTrace()
            onError.getCurrentRates()
        }
    }

}