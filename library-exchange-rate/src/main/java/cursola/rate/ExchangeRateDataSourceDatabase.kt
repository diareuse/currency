package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.database.ExchangeRateStored

internal class ExchangeRateDataSourceDatabase(
    private val database: ExchangeRateDatabase
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        return database.rates().getLatest().map(::ExchangeRate)
    }

    internal fun ExchangeRate(
        stored: ExchangeRateStored
    ) = ExchangeRate(
        currency = stored.currency,
        rate = stored.rate,
        timestamp = stored.timestamp
    )

}