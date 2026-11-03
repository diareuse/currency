package wiki.depasquale.currency.platform

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class RatesUpdatedListenerErrorBoundary(
    private val origin: RatesUpdatedListener
) : RatesUpdatedListener {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun onRatesUpdated() {
        try {
            origin.onRatesUpdated()
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
        }
    }

}
