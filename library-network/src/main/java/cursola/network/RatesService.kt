package cursola.network

interface RatesService {

    suspend fun get(): List<ExchangeRateResponse>

}