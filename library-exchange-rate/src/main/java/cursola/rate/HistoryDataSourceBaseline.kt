package cursola.rate

import java.util.Currency
import java.util.Date

internal class HistoryDataSourceBaseline(
    private val origin: HistoryDataSource
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        if (currency == Currency.getInstance("EUR"))
            return listOf(ExchangeRate(currency, 1.0, Date(0)))
        return origin.get(currency)
    }

}