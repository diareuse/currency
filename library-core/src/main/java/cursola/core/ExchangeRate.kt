package cursola.core

import cursola.core.util.todayRange
import java.util.Date

data class ExchangeRate(
    val rate: Double,
    val timestamp: Date = Date()
) {

    constructor(
        from: ExchangeRate,
        to: ExchangeRate
    ) : this(
        rate = 1.0 / from.rate * to.rate,
        timestamp = maxOf(from.timestamp, to.timestamp)
    )

    val isFromToday
        get() = timestamp in todayRange

    fun applyTo(amount: Double): Double {
        return amount * rate
    }

}
