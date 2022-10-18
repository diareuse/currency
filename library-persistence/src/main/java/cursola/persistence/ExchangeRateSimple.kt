package cursola.persistence

import androidx.room.ColumnInfo
import java.util.Date

internal data class ExchangeRateSimple(
    @ColumnInfo(name = "rate")
    val rate: Double,

    @ColumnInfo(name = "timestamp")
    val timestamp: Date
)