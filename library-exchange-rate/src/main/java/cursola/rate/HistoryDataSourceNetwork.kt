package cursola.rate

import cursola.rate.network.ExchangeRateService
import java.util.Currency

internal class HistoryDataSourceNetwork(
    private val network: ExchangeRateService,
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        return network.get().filter { it.currency == currency }.sortedByDescending { it.timestamp }
    }

}