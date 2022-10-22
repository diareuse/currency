package cursola.rate

import java.util.Currency

interface ConversionRateDataSource {

    suspend fun get(from: Currency, to: Currency): Double

}