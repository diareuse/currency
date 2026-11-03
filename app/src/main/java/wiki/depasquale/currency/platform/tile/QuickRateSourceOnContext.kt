package wiki.depasquale.currency.platform.tile

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuickRateSourceOnContext(
    private val origin: QuickRateSource,
    private val context: CoroutineContext
) : QuickRateSource {

    override suspend fun current() = withContext(context) { origin.current() }

}
