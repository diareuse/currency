package cursola.rate.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface DaoExchangeRate : DaoCreate<ExchangeRateStored>, DaoUpdate<ExchangeRateStored> {

    @Query("select rate from exchange_rates where currency=:currency order by timestamp desc limit 1")
    suspend fun get(currency: String): Double?

    @Query("select rate from exchange_rates where currency=:currency and createdAt>=:start and createdAt<=:end order by timestamp desc limit 1")
    suspend fun get(currency: String, start: Long, end: Long): Double?

    @Query("select * from exchange_rates where timestamp=(select timestamp from exchange_rates order by timestamp desc limit 1)")
    suspend fun getLatest(): List<ExchangeRateStored>

    @Query("select * from exchange_rates where currency=:currency order by timestamp desc")
    suspend fun getAll(currency: String): List<ExchangeRateStored>

}