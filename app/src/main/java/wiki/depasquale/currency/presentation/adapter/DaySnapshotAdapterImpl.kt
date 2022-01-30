package wiki.depasquale.currency.presentation.adapter

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.DaySnapshot

class DaySnapshotAdapterImpl : DaySnapshotAdapter {

    override fun adapt(rate: ExchangeRate): DaySnapshot {
        return DaySnapshot(rate.rate, rate.time)
    }

}