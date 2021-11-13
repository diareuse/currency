package org.billthefarmer.currency.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.billthefarmer.currency.domain.model.PersistedCurrency
import org.billthefarmer.currency.domain.model.PersistedRate

@Database(
    version = 1,
    entities = [
        PersistedCurrency::class,
        PersistedRate::class
    ]
)
abstract class DatabaseStorage : RoomDatabase() {

    abstract fun getRates(): DaoRate
    abstract fun getCurrencies(): DaoCurrency

    companion object {

        fun create(context: Context) = Room
            .databaseBuilder(context, DatabaseStorage::class.java, context.packageName + ".storage")
            .fallbackToDestructiveMigration()
            .build()

    }

}