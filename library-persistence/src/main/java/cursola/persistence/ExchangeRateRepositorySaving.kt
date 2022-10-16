package cursola.persistence

import cursola.core.ExchangeRate
import cursola.core.ExchangeRateRepository
import cursola.persistence.util.deferTask
import java.util.Currency

class ExchangeRateRepositorySaving(
    private val origin: ExchangeRateRepository,
    private val currencies: CurrencyDao,
    private val rates: ExchangeRateDao
) : ExchangeRateRepository by origin {

    override suspend fun get(): List<Currency> {
        return origin.get().deferTask {
            currencies.insert(it.map(::SupportedCurrency))
        }
    }

    override suspend fun get(from: Currency, to: Currency): ExchangeRate {
        return origin.get(from, to).deferTask {
            val value = ExchangeRatePersisted(from, to, it.rate, it.timestamp)
            rates.insert(value)
        }
    }

}
