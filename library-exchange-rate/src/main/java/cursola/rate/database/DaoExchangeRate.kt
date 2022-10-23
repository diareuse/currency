package cursola.rate.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface DaoExchangeRate : DaoCreate<ExchangeRateStored>, DaoUpdate<ExchangeRateStored> {

    @Query("select rate from exchange_rates where currency=:currency limit 1")
    suspend fun get(currency: String): Double?

    @Query("select * from exchange_rates where timestamp=(select timestamp from exchange_rates order by timestamp desc limit 1)")
    suspend fun getLatest(): List<ExchangeRateStored>

}