package org.billthefarmer.currency.domain.rate

import androidx.annotation.WorkerThread
import org.billthefarmer.currency.domain.model.ExchangeRate

interface ExchangeRates {

    @WorkerThread
    fun getCurrentRates(): List<ExchangeRate>

}