package cursola.persistence

import androidx.room.TypeConverter
import java.util.Date

internal class ConverterDate {

    @TypeConverter
    fun from(timestamp: Long): Date =
        Date(timestamp)

    @TypeConverter
    fun to(date: Date): Long =
        date.time

}