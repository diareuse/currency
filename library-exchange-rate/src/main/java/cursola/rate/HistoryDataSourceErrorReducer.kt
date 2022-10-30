package cursola.rate

import java.util.Currency

internal class HistoryDataSourceErrorReducer(
    private vararg val sources: HistoryDataSource
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        for (source in sources) {
            try {
                return source.get(currency)
            } catch (e: Throwable) {
                e.printStackTrace()
                continue
            }
        }
        return emptyList()
    }

}