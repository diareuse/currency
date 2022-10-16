package cursola.persistence

import androidx.room.Dao
import androidx.room.Query
import cursola.persistence.database.DaoCreate
import java.util.Currency

@Dao
interface CurrencyDao : DaoCreate<SupportedCurrency> {

    @Query("select distinct(currency) from currencies")
    suspend fun select(): List<Currency>

}