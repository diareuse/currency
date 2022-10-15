package cursola.core

import cursola.core.util.todayRange
import java.util.Date

data class ExchangeRate(
    val rate: Double,
    val timestamp: Date = Date()
) {

    val isFromToday
        get() = timestamp in todayRange

    fun applyTo(amount: Double): Double {
        return amount * rate
    }

}
