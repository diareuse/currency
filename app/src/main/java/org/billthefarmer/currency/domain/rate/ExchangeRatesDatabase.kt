package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.database.DaoRate
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.time.TimeRangeFactory

class ExchangeRatesDatabase(
    private val rate: DaoRate,
    private val adapter: ExchangeRatesAdapter,
    private val timeRange: TimeRangeFactory
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val range = timeRange.getTimeRange()
        val stored = rate.getRatesInRange(range.first, range.last)
        return stored.map(adapter::adapt)
    }

}