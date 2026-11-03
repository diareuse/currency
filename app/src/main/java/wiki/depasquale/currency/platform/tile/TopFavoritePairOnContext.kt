package wiki.depasquale.currency.platform.tile

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TopFavoritePairOnContext(
    private val origin: TopFavoritePair,
    private val context: CoroutineContext
) : TopFavoritePair {

    override suspend fun current() = withContext(context) { origin.current() }

}
