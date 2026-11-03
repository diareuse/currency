package wiki.depasquale.currency.platform.widget

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class WidgetUpdaterErrorBoundary(
    private val origin: WidgetUpdater
) : WidgetUpdater {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun update() {
        try {
            origin.update()
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
        }
    }

}
