package cursola.persistence

import androidx.room.TypeConverter
import java.util.Currency

class ConverterCurrency {

    @TypeConverter
    fun from(code: String): Currency =
        Currency.getInstance(code)

    @TypeConverter
    fun to(currency: Currency): String =
        currency.currencyCode

}