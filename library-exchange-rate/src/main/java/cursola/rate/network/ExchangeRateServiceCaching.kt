package cursola.rate.network

import cursola.rate.ExchangeRate
import cursola.rate.util.isToday
import java.util.Date

internal class ExchangeRateServiceCaching(
    private val origin: ExchangeRateService
) : ExchangeRateService {

    private var timestamp: Date? = null
    private var items: List<ExchangeRate> = emptyList()

    override suspend fun get(): List<ExchangeRate> {
        if (timestamp?.isToday == true)
            return items

        return origin.get().also {
            timestamp = Date()
            items = it
        }
    }

}