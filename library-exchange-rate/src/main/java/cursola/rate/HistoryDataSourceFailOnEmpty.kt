package cursola.rate

import java.util.Currency

internal class HistoryDataSourceFailOnEmpty(
    private val origin: HistoryDataSource
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        return origin.get(currency).also {
            check(it.isNotEmpty())
        }
    }

}