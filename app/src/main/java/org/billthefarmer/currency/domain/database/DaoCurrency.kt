package org.billthefarmer.currency.domain.database

import androidx.room.Dao
import androidx.room.Insert
import org.billthefarmer.currency.domain.model.PersistedCurrency

@Dao
interface DaoCurrency {

    @Insert
    fun insert(item: PersistedCurrency)

}