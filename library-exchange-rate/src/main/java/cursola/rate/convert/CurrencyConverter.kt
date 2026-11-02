package cursola.rate.convert

import cursola.rate.ExchangeRateError
import java.util.Currency

interface CurrencyConverter {

    @Throws(ExchangeRateError.NotFoundException::class)
    suspend fun convert(amount: Money, to: Currency): Conversion

    suspend fun convertToFavorites(amount: Money): List<Conversion>

}
