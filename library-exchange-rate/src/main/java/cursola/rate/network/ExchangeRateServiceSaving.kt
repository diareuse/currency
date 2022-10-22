package cursola.rate.network

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.database.ExchangeRateStored
import cursola.rate.util.runEffect

internal class ExchangeRateServiceSaving(
    private val origin: ExchangeRateService,
    private val database: ExchangeRateDatabase
) : ExchangeRateService {

    override suspend fun get() = origin.get().runEffect {
        for (rate in it) {
            with(database.rates()) {
                insert(ExchangeRateStored(rate))
            }
        }
    }

}