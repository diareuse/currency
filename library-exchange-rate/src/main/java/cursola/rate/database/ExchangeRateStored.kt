package cursola.rate.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import cursola.rate.ExchangeRate
import java.util.Currency
import java.util.Date

@Entity(
    tableName = "exchange_rates",
    primaryKeys = ["currency", "timestamp"]
)
internal data class ExchangeRateStored(
    @ColumnInfo(name = "rate")
    val rate: Double,
    @ColumnInfo(name = "currency")
    val currency: Currency,
    @ColumnInfo(name = "timestamp")
    val timestamp: Date
) {

    constructor(
        rate: ExchangeRate,
        timestamp: Date = rate.timestamp
    ) : this(
        rate = rate.rate,
        currency = rate.currency,
        timestamp = timestamp
    )

}