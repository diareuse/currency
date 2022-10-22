package cursola.rate

interface ExchangeRateDataSource {

    suspend fun get(): List<ExchangeRate>

}