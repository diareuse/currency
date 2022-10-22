package cursola.rate.network

import cursola.rate.ExchangeRate
import java.io.IOException

internal interface ExchangeRateService {

    @Throws(IOException::class)
    suspend fun get(): List<ExchangeRate>

}