package wiki.depasquale.currency.platform.widget

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WidgetUpdaterOnContext(
    private val origin: WidgetUpdater,
    private val context: CoroutineContext
) : WidgetUpdater {

    override suspend fun update() = withContext(context) { origin.update() }

}
