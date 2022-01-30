package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.adapter.ExchangeRatesAdapter
import wiki.depasquale.currency.domain.database.DaoRate
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.time.TimeRangeFactory

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