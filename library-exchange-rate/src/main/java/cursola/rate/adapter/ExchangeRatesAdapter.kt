package cursola.rate.adapter

import cursola.rate.ExchangeRate
import java.io.InputStream

interface ExchangeRatesAdapter {

    suspend fun adapt(stream: InputStream): List<ExchangeRate>

}