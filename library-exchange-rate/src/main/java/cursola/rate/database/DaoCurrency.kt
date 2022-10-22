package cursola.rate.database

import androidx.room.Dao

@Dao
internal interface DaoCurrency : DaoCreate<CurrencyStored>, DaoUpdate<CurrencyStored>