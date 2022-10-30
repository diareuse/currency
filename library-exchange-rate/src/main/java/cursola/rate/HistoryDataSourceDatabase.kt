package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import java.util.Currency

internal class HistoryDataSourceDatabase(
    private val database: ExchangeRateDatabase
) : HistoryDataSource {

    override suspend fun get(currency: Currency): List<ExchangeRate> {
        return database.rates().getAll(currency.currencyCode).map(::ExchangeRate)
    }

}