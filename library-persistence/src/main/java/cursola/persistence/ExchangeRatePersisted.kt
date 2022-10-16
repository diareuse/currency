package cursola.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Currency
import java.util.Date

@Entity(
    tableName = "rates",
    primaryKeys = ["from", "to"],
    foreignKeys = [
        ForeignKey(
            entity = SupportedCurrency::class,
            parentColumns = ["currency"],
            childColumns = ["from"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SupportedCurrency::class,
            parentColumns = ["currency"],
            childColumns = ["to"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("timestamp", orders = [Index.Order.DESC])
    ]
)
data class ExchangeRatePersisted(
    @ColumnInfo(name = "from")
    val from: Currency,

    @ColumnInfo(name = "to")
    val to: Currency,

    @ColumnInfo(name = "rate")
    val rate: Double,

    @ColumnInfo(name = "timestamp")
    val timestamp: Date
)