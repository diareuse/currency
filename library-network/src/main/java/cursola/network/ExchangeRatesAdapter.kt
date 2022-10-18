package cursola.network

import java.io.InputStream

internal interface ExchangeRatesAdapter {

    fun adapt(stream: InputStream): List<ExchangeRateResponse>

}