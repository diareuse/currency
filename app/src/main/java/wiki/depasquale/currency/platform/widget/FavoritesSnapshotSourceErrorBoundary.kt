package wiki.depasquale.currency.platform.widget

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class FavoritesSnapshotSourceErrorBoundary(
    private val origin: FavoritesSnapshotSource
) : FavoritesSnapshotSource {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun current(): FavoritesSnapshot = try {
        origin.current()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        FavoritesSnapshot.Unavailable(null)
    }

}
