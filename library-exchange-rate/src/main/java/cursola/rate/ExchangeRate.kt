package cursola.rate

import java.util.Currency
import java.util.Date

data class ExchangeRate(
    val currency: Currency,
    val rate: Double,
    val timestamp: Date
)