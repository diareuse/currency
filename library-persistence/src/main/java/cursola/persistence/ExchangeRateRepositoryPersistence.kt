package cursola.persistence

import cursola.core.ExchangeRateRepository

class ExchangeRateRepositoryPersistence(
    private val storage: Storage
) {

    fun wrap(
        other: ExchangeRateRepository
    ): ExchangeRateRepository {
        var result = other
        result = ExchangeRateRepositorySaving(result, storage.currency(), storage.rates())
        return result
    }

    fun storage(): ExchangeRateRepository {
        val result: ExchangeRateRepository
        result = ExchangeRateRepositoryStorage(storage.currency(), storage.rates())
        return result
    }

}