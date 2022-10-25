package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.util.todayRange
import java.util.Currency

internal class ConversionRateDataSourceDatabaseToday(
    private val database: ExchangeRateDatabase
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        val rates = database.rates()
        val today = todayRange
        val start = today.start.time
        val end = today.endInclusive.time
        val fromRate = rates.get(from.currencyCode, start, end)
        val toRate = rates.get(to.currencyCode, start, end)

        if (fromRate == null || toRate == null)
            throw ExchangeRateError.NotFoundException("Database doesn't provide conversion $from($fromRate) -> $to($toRate)")

        return ExchangeRate.getExchangeRate(fromRate, toRate)
    }

}