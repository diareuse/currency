package wiki.depasquale.currency.presentation.adapter

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.DaySnapshot

interface DaySnapshotAdapter {

    fun adapt(rate: ExchangeRate): DaySnapshot

}