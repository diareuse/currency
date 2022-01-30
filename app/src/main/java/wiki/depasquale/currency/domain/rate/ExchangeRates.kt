package wiki.depasquale.currency.domain.rate

import androidx.annotation.WorkerThread
import wiki.depasquale.currency.domain.model.ExchangeRate

interface ExchangeRates {

    @WorkerThread
    fun getCurrentRates(): List<ExchangeRate>

}