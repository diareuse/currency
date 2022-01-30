package wiki.depasquale.currency.domain.adapter

import androidx.annotation.WorkerThread
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.model.PersistedCurrency
import wiki.depasquale.currency.domain.model.PersistedRate
import java.io.InputStream

interface ExchangeRatesAdapter {

    @WorkerThread
    fun adapt(stream: InputStream): List<ExchangeRate>

    @WorkerThread
    fun adapt(rate: PersistedRate): ExchangeRate

    @WorkerThread
    fun adapt(rate: ExchangeRate): PersistedRate

    @WorkerThread
    fun adaptCurrency(rate: ExchangeRate): PersistedCurrency

}