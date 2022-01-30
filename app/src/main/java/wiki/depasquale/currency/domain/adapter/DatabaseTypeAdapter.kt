package wiki.depasquale.currency.domain.adapter

import androidx.room.TypeConverter
import java.util.*

class DatabaseTypeAdapter {

    @TypeConverter
    fun fromDatabase(time: Long) = Date(time)

    @TypeConverter
    fun toDatabase(date: Date) = date.time

    // ---

    @TypeConverter
    fun fromDatabase(currency: String) = requireNotNull(Currency.getInstance(currency))

    @TypeConverter
    fun toDatabase(currency: Currency) = requireNotNull(currency.currencyCode)

}