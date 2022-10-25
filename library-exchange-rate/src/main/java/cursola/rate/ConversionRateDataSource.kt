package cursola.rate

import java.util.Currency

interface ConversionRateDataSource {

    @Throws(ExchangeRateError.NotFoundException::class)
    suspend fun get(from: Currency, to: Currency): Double

}