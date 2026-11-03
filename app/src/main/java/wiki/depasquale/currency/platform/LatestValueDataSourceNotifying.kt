package wiki.depasquale.currency.platform

import cursola.rate.LatestValueDataSource

class LatestValueDataSourceNotifying(
    private val origin: LatestValueDataSource,
    private val listener: LatestValueChangedListener
) : LatestValueDataSource {

    override var currency: String
        get() = origin.currency
        set(value) {
            if (value == origin.currency) return
            origin.currency = value
            listener.onLatestValueChanged()
        }

    override var value: String
        get() = origin.value
        set(value) {
            if (value == origin.value) return
            origin.value = value
            listener.onLatestValueChanged()
        }

}
