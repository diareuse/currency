package cursola.rate.database

import androidx.room.TypeConverter
import java.util.Date

class TypeConverterDate : AbstractTypeConverter<Date, Long>() {

    @TypeConverter
    override fun to(type: Date): Long {
        return type.time
    }

    @TypeConverter
    override fun from(primitive: Long): Date {
        return Date(primitive)
    }

}