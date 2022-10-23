package cursola.rate.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import cursola.rate.database.ExchangeRateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityRetainedComponent::class)
@Module
internal class DatabaseModule {

    @Provides
    fun database(
        @ApplicationContext
        context: Context
    ): ExchangeRateDatabase {
        val klass = ExchangeRateDatabase::class.java
        val name = context.packageName + ".exchange-rate"
        return Room.databaseBuilder(context, klass, name)
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

}