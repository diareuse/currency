package cursola.persistence.database

import cursola.persistence.CurrencyDao
import cursola.persistence.ExchangeRateDao
import cursola.persistence.ExchangeRatePersisted
import cursola.persistence.ExchangeRateSimple
import cursola.persistence.Storage
import cursola.persistence.SupportedCurrency
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import java.util.Currency

fun StorageImpl(): Storage {
    val storage: Storage = mock()
    whenever(storage.rates()).thenReturn(spy<ExchangeRateDao>(ExchangeRateDaoImpl()))
    whenever(storage.currency()).thenReturn(spy<CurrencyDao>(CurrencyDaoImpl()))
    return storage
}

private open class ExchangeRateDaoImpl : ExchangeRateDao {
    private val items: MutableList<ExchangeRatePersisted> = mutableListOf()

    override suspend fun select(from: String, to: String): ExchangeRateSimple? {
        val result = items.firstOrNull { it.from.currencyCode == from && it.to.currencyCode == to }
            ?: return null
        return ExchangeRateSimple(result.rate, result.timestamp)
    }

    override suspend fun insert(value: ExchangeRatePersisted) {
        items += value
    }

    override suspend fun insert(values: Iterable<ExchangeRatePersisted>) {
        items.addAll(values)
    }

}

private open class CurrencyDaoImpl : CurrencyDao {

    private val items: MutableList<SupportedCurrency> = mutableListOf()

    override suspend fun select(): List<Currency> {
        return items.map { it.currency }
    }

    override suspend fun insert(value: SupportedCurrency) {
        items += value
    }

    override suspend fun insert(values: Iterable<SupportedCurrency>) {
        items.addAll(values)
    }

}