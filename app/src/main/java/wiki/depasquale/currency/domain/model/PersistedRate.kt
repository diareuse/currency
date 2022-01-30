package wiki.depasquale.currency.domain.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import wiki.depasquale.currency.domain.adapter.DatabaseTypeAdapter
import java.util.*

@Entity(
    tableName = "rates",
    foreignKeys = [
        ForeignKey(
            entity = PersistedCurrency::class,
            parentColumns = ["currency"],
            childColumns = ["currency"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("currency", "rate", "time", unique = true)
    ]
)
@TypeConverters(DatabaseTypeAdapter::class)
data class PersistedRate(
    @ColumnInfo(name = "currency") val currency: Currency,
    @ColumnInfo(name = "rate") val rate: Double,
    @ColumnInfo(name = "time") val time: Date,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0
)
