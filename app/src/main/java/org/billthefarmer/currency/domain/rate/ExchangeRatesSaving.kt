package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.database.DaoCurrency
import org.billthefarmer.currency.domain.database.DaoRate
import org.billthefarmer.currency.domain.model.ExchangeRate

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