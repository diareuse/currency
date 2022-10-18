package cursola.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Currency

@Entity(
    tableName = "currencies"
)
internal data class SupportedCurrency(
    @PrimaryKey
    @ColumnInfo(name = "currency")
    val currency: Currency
)