package cursola.core

import java.util.Currency

class ExchangeRateRepositoryRequireNotEmpty(
    private val origin: ExchangeRateRepository
) : ExchangeRateRepository by origin {

    override suspend fun get(): List<Currency> {
        return origin.get().also {
            require(it.isNotEmpty())
        }
    }

}
