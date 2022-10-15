package cursola.core

import java.util.Currency

interface ExchangeRateRepository {

    suspend fun get(): List<Currency>
    suspend fun get(from: Currency, to: Currency): ExchangeRate

}
