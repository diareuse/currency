package cursola.network

import java.io.InputStream

interface ExchangeRatesAdapter {

    fun adapt(stream: InputStream): List<ExchangeRateResponse>

}