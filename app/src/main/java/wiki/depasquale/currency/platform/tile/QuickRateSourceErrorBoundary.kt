package wiki.depasquale.currency.platform.tile

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class QuickRateSourceErrorBoundary(
    private val origin: QuickRateSource
) : QuickRateSource {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun current(): QuickRate = try {
        origin.current()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        QuickRate.Unavailable(null)
    }

}
