package wiki.depasquale.currency.platform.tile

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import wiki.depasquale.currency.platform.link.CurrencyPair

class TopFavoritePairErrorBoundary(
    private val origin: TopFavoritePair
) : TopFavoritePair {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun current(): CurrencyPair? = try {
        origin.current()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        null
    }

}
