package cursola.rate.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import cursola.rate.util.todayRange

@TypeConverters(
    TypeConverterCurrency::class,
    TypeConverterDate::class
)
@Database(
    version = 2,
    exportSchema = true,
    entities = [
        ExchangeRateStored::class,
        FavoriteCurrency::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = MigrationSpec1to2::class)
    ]
)
internal abstract class ExchangeRateDatabase : RoomDatabase() {

    abstract fun rates(): DaoExchangeRate
    abstract fun favorites(): DaoFavorite

}

class MigrationSpec1to2 : AutoMigrationSpec {
    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        val time = todayRange.start.time
        db.execSQL("update exchange_rates set createdAt = ?", arrayOf(time))
    }
}