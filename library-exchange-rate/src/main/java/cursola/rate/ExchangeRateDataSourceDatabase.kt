package cursola.rate

import cursola.rate.database.ExchangeRateDatabase

internal class ExchangeRateDataSourceDatabase(
    private val database: ExchangeRateDatabase
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        return database.rates().getLatest()
            .map(::ExchangeRate)
            .distinctBy { it.currency }
    }

}