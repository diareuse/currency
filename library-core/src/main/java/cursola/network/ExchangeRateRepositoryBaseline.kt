package cursola.network

import java.util.Currency

class ExchangeRateRepositoryBaseline(
    private val origin: ExchangeRateRepository,
    private val baseline: Currency
) : ExchangeRateRepository by origin {

    override suspend fun get(): List<Currency> {
        return buildList {
            add(baseline)
            addAll(origin.get())
        }
    }

}