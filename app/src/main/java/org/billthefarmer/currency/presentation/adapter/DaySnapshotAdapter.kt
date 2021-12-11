package org.billthefarmer.currency.presentation.adapter

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.DaySnapshot

interface DaySnapshotAdapter {

    fun adapt(rate: ExchangeRate): DaySnapshot

}