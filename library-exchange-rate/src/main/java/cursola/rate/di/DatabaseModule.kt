package cursola.rate.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import cursola.rate.database.ExchangeRateDatabase
import spike.Include
import spike.Singleton

@Singleton
@Include
internal fun database(
    context: Context
): ExchangeRateDatabase {
    val klass = ExchangeRateDatabase::class.java
    val name = context.packageName + ".exchange-rate"
    return Room.databaseBuilder(context, klass, name)
        .fallbackToDestructiveMigration()
        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .build()
}
