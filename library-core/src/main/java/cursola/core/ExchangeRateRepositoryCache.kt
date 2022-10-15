package cursola.core

import java.util.Currency

class ExchangeRateRepositoryCache(
    private val origin: ExchangeRateRepository
) : ExchangeRateRepository {

    private var list: List<Currency>? = null
    private var rates: MutableMap<CurrencyKey, ExchangeRate> = mutableMapOf()

    override suspend fun get(): List<Currency> {
        return list ?: origin.get().also {
            list = it
        }
    }

    override suspend fun get(from: Currency, to: Currency): ExchangeRate {
        return rates.getOrPut(CurrencyKey(from, to)) {
            origin.get(from, to)
        }
    }

    private class CurrencyKey(
        vararg val currencies: Currency
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is CurrencyKey) return false

            if (!currencies.contentEquals(other.currencies)) return false

            return true
        }

        override fun hashCode(): Int {
            return currencies.contentHashCode()
        }

    }

}
