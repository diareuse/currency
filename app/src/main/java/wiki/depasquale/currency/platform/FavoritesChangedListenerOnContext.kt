package wiki.depasquale.currency.platform

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FavoritesChangedListenerOnContext(
    private val origin: FavoritesChangedListener,
    private val context: CoroutineContext
) : FavoritesChangedListener {

    override suspend fun onFavoritesChanged() = withContext(context) { origin.onFavoritesChanged() }

}
