package cursola.persistence

import cursola.core.ExchangeRate
import cursola.core.ExchangeRateRepository
import java.util.Currency

class ExchangeRateRepositoryStorage(
    private val currencies: CurrencyDao,
    private val rates: ExchangeRateDao
) : ExchangeRateRepository {

    override suspend fun get(): List<Currency> {
        return currencies.select()
    }

    override suspend fun get(from: Currency, to: Currency): ExchangeRate {
        val value = rates.select(from.currencyCode, to.currencyCode)
        return ExchangeRate(requireNotNull(value))
    }

}
