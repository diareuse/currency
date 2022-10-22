package cursola.rate

import cursola.rate.network.ExchangeRateService

internal class ExchangeRateDataSourceNetwork(
    private val network: ExchangeRateService
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        return network.get()
    }

}