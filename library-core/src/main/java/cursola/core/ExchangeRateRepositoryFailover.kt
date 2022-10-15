package cursola.core

import java.util.Currency

class ExchangeRateRepositoryFailover(
    private val primary: ExchangeRateRepository,
    private val secondary: ExchangeRateRepository
) : ExchangeRateRepository {

    override suspend fun get(): List<Currency> {
        return primary.runCatching { get() }
            .recover { secondary.get() }
            .getOrThrow()
    }

    override suspend fun get(from: Currency, to: Currency): ExchangeRate {
        return primary.runCatching { get(from, to) }
            .recover { secondary.get(from, to) }
            .getOrThrow()
    }

}
