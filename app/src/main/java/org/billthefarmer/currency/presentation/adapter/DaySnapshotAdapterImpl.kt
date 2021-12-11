package org.billthefarmer.currency.presentation.adapter

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.DaySnapshot

class DaySnapshotAdapterImpl : DaySnapshotAdapter {

    override fun adapt(rate: ExchangeRate): DaySnapshot {
        return DaySnapshot(rate.rate, rate.time)
    }

}