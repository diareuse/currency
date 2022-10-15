package cursola.network

import cursola.network.util.todayRange
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
