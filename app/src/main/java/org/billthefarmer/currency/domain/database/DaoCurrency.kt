package org.billthefarmer.currency.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.billthefarmer.currency.domain.model.PersistedCurrency

@Dao
interface DaoCurrency {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: PersistedCurrency)

}