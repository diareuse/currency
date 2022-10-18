package cursola.network

import cursola.core.ExchangeRate
import cursola.core.ExchangeRateRepository
import java.util.Currency

internal class ExchangeRateRepositoryNetwork(
    private val service: RatesService
) : ExchangeRateRepository {

    override suspend fun get(): List<Currency> {
        return service.get().map { it.currency }
    }

    override suspend fun get(from: Currency, to: Currency): ExchangeRate {
        val rates = service.get()
        val fromRate = rates.find { it.currency == from }
        val toRate = rates.find { it.currency == to }
        requireNotNull(fromRate) { "Cannot convert values from ${from.currencyCode}" }
        requireNotNull(toRate) { "Cannot convert values to ${to.currencyCode}" }
        return ExchangeRate(
            from = ExchangeRate(fromRate),
            to = ExchangeRate(toRate)
        )
    }

}
