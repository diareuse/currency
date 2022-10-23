package cursola.rate.network

import cursola.rate.ExchangeRate
import java.util.Currency
import java.util.Date

internal class ExchangeRateServiceBaseline(
    private val origin: ExchangeRateService
) : ExchangeRateService {

    override suspend fun get(): List<ExchangeRate> {
        return buildList {
            add(ExchangeRate(Currency.getInstance("EUR"), 1.0, Date(0)))
            addAll(origin.get())
        }
    }

}