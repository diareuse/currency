package wiki.depasquale.currency.platform

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class FavoritesChangedListenerErrorBoundary(
    private val origin: FavoritesChangedListener
) : FavoritesChangedListener {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun onFavoritesChanged() {
        try {
            origin.onFavoritesChanged()
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
        }
    }

}
