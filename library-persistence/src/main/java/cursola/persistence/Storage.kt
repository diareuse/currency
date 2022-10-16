@file:OptIn(ExperimentalRoomApi::class)

package cursola.persistence

import android.content.Context
import androidx.annotation.OptIn
import androidx.room.Database
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.util.concurrent.TimeUnit

@TypeConverters(
    ConverterCurrency::class,
    ConverterDate::class
)
@Database(
    version = 1,
    exportSchema = true,
    autoMigrations = [],
    entities = [
        ExchangeRatePersisted::class,
        SupportedCurrency::class
    ]
)
abstract class Storage : RoomDatabase() {

    abstract fun rates(): ExchangeRateDao

    companion object {

        operator fun invoke(context: Context): Storage {
            val type = Storage::class.java
            val name = context.packageName + ".storage"
            return Room
                .databaseBuilder(context, type, name)
                .enableMultiInstanceInvalidation()
                .setAutoCloseTimeout(10, TimeUnit.SECONDS)
                .build()
        }

    }

}