package cursola.rate

import java.util.Currency

internal class HistoryDataSourceSizeGuard(
    private val origin: HistoryDataSource,
    private val limitSize: Int
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        return origin.get(currency).also {
            check(it.size >= limitSize) {
                "Expected to contain at least $limitSize elements for $currency, but was ${it}."
            }
        }
    }

}