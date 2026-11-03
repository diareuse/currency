package wiki.depasquale.currency.platform.widget

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FavoritesSnapshotSourceOnContext(
    private val origin: FavoritesSnapshotSource,
    private val context: CoroutineContext
) : FavoritesSnapshotSource {

    override suspend fun current() = withContext(context) { origin.current() }

}
