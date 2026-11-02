package cursola.rate.convert

import cursola.rate.ExchangeRateDataSource
import java.util.Date

class RatesTimestampExchangeRate(
    private val source: ExchangeRateDataSource
) : RatesTimestamp {

    override suspend fun get(): Date? = source.get().maxOfOrNull { it.timestamp }

}
