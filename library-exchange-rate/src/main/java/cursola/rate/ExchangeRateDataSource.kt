package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.database.ExchangeRateStored
import cursola.rate.network.ExchangeRateService

interface ExchangeRateDataSource {

    suspend fun get(): List<ExchangeRate>

}

internal class ExchangeRateDataSourceImpl(
    private val network: ExchangeRateService,
    private val database: ExchangeRateDatabase
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        return try {
            network.get()
        } catch (e: Throwable) {
            try {
                database.rates().getLatest().map(::ExchangeRate)
            } catch (e: Throwable) {
                emptyList()
            }
        }
    }

}

internal fun ExchangeRate(stored: ExchangeRateStored): ExchangeRate {
    return ExchangeRate(
        currency = stored.currency,
        rate = stored.rate,
        timestamp = stored.timestamp
    )
}