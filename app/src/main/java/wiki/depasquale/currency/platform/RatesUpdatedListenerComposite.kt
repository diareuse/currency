package wiki.depasquale.currency.platform

class RatesUpdatedListenerComposite(
    private val listeners: List<RatesUpdatedListener>
) : RatesUpdatedListener {

    override suspend fun onRatesUpdated() {
        listeners.forEach { it.onRatesUpdated() }
    }

}
