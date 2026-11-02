package cursola.rate.convert

import cursola.rate.ExchangeRateError
import java.util.Currency
import java.util.Locale

interface RatesQuery {

    suspend fun currencies(locale: Locale): List<CurrencyInfo>

    @Throws(ExchangeRateError.NotFoundException::class)
    suspend fun latest(base: Currency): List<Conversion>

}
