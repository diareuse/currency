package cursola.rate

import cursola.rate.util.isToday

internal class ExchangeRateDataSourceTodayGuard(
    private val origin: ExchangeRateDataSource
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        val items = origin.get()
        val date = items.firstOrNull()?.timestamp
        if (date != null && date.isToday)
            return items
        throw IllegalStateException("Old data")
    }

}