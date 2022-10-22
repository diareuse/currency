package cursola.rate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(
    TypeConverterCurrency::class,
    TypeConverterDate::class
)
@Database(
    version = 1,
    entities = [
        ExchangeRateStored::class,
        FavoriteCurrency::class
    ]
)
internal abstract class ExchangeRateDatabase : RoomDatabase() {

    abstract fun rates(): DaoExchangeRate
    abstract fun favorites(): DaoFavorite

}