package wiki.depasquale.currency.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import wiki.depasquale.currency.domain.model.PersistedCurrency

@Dao
interface DaoCurrency {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: PersistedCurrency)

}