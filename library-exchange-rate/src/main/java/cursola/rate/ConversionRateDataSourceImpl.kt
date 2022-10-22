package cursola.rate

import cursola.rate.database.CurrencyStored
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.database.ExchangeRateStored
import cursola.rate.network.ExchangeRateService
import java.util.Currency

internal class ConversionRateDataSourceImpl(
    private val network: ExchangeRateService,
    private val database: ExchangeRateDatabase
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        return try {
            val data = network.get()
            with(database.currencies()) {
                for (rate in data)
                    insert(CurrencyStored(rate.currency))
            }
            with(database.rates()) {
                for (rate in data)
                    insert(ExchangeRateStored(rate))
            }
            1 / data.first { it.currency == from }.rate * data.first { it.currency == to }.rate
        } catch (e: Throwable) {
            try {
                val fromRate = database.rates().get(from.currencyCode)
                val toRate = database.rates().get(to.currencyCode)
                1 / requireNotNull(fromRate) * requireNotNull(toRate)
            } catch (e: Throwable) {
                throw ExchangeRateError.NotFoundException(
                    e,
                    "Cannot find converstion from $from to $to"
                )
            }
        }
    }

}