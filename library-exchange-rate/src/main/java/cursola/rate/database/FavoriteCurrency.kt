package cursola.rate.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Currency

@Entity(
    tableName = "favorites",
    indices = [
        Index(value = ["priority"], orders = [Index.Order.DESC])
    ]
)
internal data class FavoriteCurrency(
    @PrimaryKey
    @ColumnInfo(name = "currency")
    val currency: Currency,

    @ColumnInfo(name = "priority")
    val priority: Int
)