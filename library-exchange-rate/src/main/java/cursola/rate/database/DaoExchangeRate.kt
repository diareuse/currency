package cursola.rate.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface DaoExchangeRate : DaoCreate<ExchangeRateStored>, DaoUpdate<ExchangeRateStored> {

    @Query("select rate from exchange_rates where currency=:currency limit 1")
    fun get(currency: String): Double?

}