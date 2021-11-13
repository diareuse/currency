package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.database.DaoRate
import org.billthefarmer.currency.domain.model.ExchangeRate
import java.util.*
import java.util.concurrent.TimeUnit

class ExchangeRatesDatabase(
    private val rate: DaoRate,
    private val adapter: ExchangeRatesAdapter
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val dayStart = getDayStart()
        val stored = rate.getRatesInRange(dayStart, dayStart + TimeUnit.DAYS.toMillis(1))
        return stored.map(adapter::adapt)
    }

    private fun getDayStart(): Long {
        return Calendar.getInstance().also {
            it[Calendar.HOUR_OF_DAY] = 0
            it[Calendar.MINUTE] = 0
            it[Calendar.SECOND] = 0
            it[Calendar.MILLISECOND] = 0
        }.timeInMillis
    }

}