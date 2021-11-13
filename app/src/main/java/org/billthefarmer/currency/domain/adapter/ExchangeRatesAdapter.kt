package org.billthefarmer.currency.domain.adapter

import androidx.annotation.WorkerThread
import org.billthefarmer.currency.domain.model.ExchangeRate
import java.io.InputStream

interface ExchangeRatesAdapter {

    @WorkerThread
    fun adapt(stream: InputStream): List<ExchangeRate>

}