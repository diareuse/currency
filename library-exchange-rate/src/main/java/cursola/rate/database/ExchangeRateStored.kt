package cursola.rate.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import cursola.rate.ExchangeRate
import cursola.rate.util.todayRange
import java.util.Currency
import java.util.Date

@Entity(
    tableName = "exchange_rates",
    primaryKeys = ["currency", "timestamp"],
    indices = [
        Index("timestamp", orders = [Index.Order.DESC]),
        Index("createdAt", orders = [Index.Order.DESC])
    ]
)
internal data class ExchangeRateStored(
    @ColumnInfo(name = "rate")
    val rate: Double,
    @ColumnInfo(name = "currency")
    val currency: Currency,
    @ColumnInfo(name = "timestamp")
    val timestamp: Date,
    @ColumnInfo(name = "createdAt", defaultValue = "0")
    val createdAt: Date = todayRange.start
) {

    constructor(
        rate: ExchangeRate
    ) : this(
        rate = rate.rate,
        currency = rate.currency,
        timestamp = rate.timestamp
    )

}