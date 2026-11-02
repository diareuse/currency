package cursola.rate.convert

import java.util.Date

data class Conversion(
    val source: Money,
    val result: Money,
    val rate: Double,
    val timestamp: Date?
)
