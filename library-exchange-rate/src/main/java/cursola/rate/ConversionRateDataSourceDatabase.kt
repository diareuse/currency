package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import java.util.Currency

internal class ConversionRateDataSourceDatabase(
    private val database: ExchangeRateDatabase
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        val rates = database.rates()
        val fromRate = rates.get(from.currencyCode)
        val toRate = rates.get(to.currencyCode)

        if (fromRate == null || toRate == null)
            throw ExchangeRateError.NotFoundException("Database doesn't provide conversion $from -> $to")

        return ExchangeRate.getExchangeRate(fromRate, toRate)
    }

}