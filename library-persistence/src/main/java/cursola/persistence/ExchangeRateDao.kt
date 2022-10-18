package cursola.persistence

import androidx.room.Dao
import androidx.room.Query
import cursola.persistence.database.DaoCreate

@Dao
internal interface ExchangeRateDao : DaoCreate<ExchangeRatePersisted> {

    @Query("select rate, timestamp from rates where `from`=:from and `to`=:to order by timestamp desc limit 1")
    suspend fun select(from: String, to: String): ExchangeRateSimple?

}