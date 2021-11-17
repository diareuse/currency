package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.database.DaoRate
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.tooling.setToDayEnd
import org.billthefarmer.currency.domain.tooling.setToDayStart
import java.util.*

class ExchangeRatesDatabase(
    private val rate: DaoRate,
    private val adapter: ExchangeRatesAdapter
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val dayStart = Calendar.getInstance().setToDayStart().timeInMillis
        val dayEnd = Calendar.getInstance().setToDayEnd().timeInMillis
        val stored = rate.getRatesInRange(dayStart, dayEnd)
        return stored.map(adapter::adapt)
    }

}