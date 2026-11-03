package cursola.rate

import java.util.Currency
import kotlin.coroutines.cancellation.CancellationException

internal class HistoryDataSourceErrorReducer(
    private vararg val sources: HistoryDataSource
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        for (source in sources) {
            try {
                return source.get(currency)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                e.printStackTrace()
                continue
            }
        }
        return emptyList()
    }

}