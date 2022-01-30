package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.adapter.ExchangeRatesAdapter
import wiki.depasquale.currency.domain.database.DaoCurrency
import wiki.depasquale.currency.domain.database.DaoRate
import wiki.depasquale.currency.domain.model.ExchangeRate

class ExchangeRatesSaving(
    private val source: ExchangeRates,
    private val currency: DaoCurrency,
    private val rate: DaoRate,
    private val adapter: ExchangeRatesAdapter
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        return source.getCurrentRates().onEach {
            currency.insert(adapter.adaptCurrency(it))
            rate.insert(adapter.adapt(it))
        }
    }

}