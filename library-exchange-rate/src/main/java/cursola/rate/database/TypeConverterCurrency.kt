package cursola.rate.database

import androidx.room.TypeConverter
import java.util.Currency

class TypeConverterCurrency : AbstractTypeConverter<Currency, String>() {

    @TypeConverter
    override fun to(type: Currency): String {
        return type.currencyCode
    }

    @TypeConverter
    override fun from(primitive: String): Currency {
        return Currency.getInstance(primitive)
    }

}