package cursola.rate.network

import cursola.rate.ExchangeRate
import java.util.Calendar
import java.util.Date

internal class ExchangeRateServiceCaching(
    private val origin: ExchangeRateService
) : ExchangeRateService {

    private var timestamp: Date? = null
    private var items: List<ExchangeRate> = emptyList()

    private val Date.isToday: Boolean
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.HOUR, 0)
            val start = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.add(Calendar.MILLISECOND, -1)
            val end = calendar.time
            return this in start..end
        }

    override suspend fun get(): List<ExchangeRate> {
        if (timestamp?.isToday == true)
            return items

        return origin.get().also {
            timestamp = Date()
            items = it
        }
    }

}