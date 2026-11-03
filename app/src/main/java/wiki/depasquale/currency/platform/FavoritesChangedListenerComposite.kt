package wiki.depasquale.currency.platform

class FavoritesChangedListenerComposite(
    private val listeners: List<FavoritesChangedListener>
) : FavoritesChangedListener {

    override suspend fun onFavoritesChanged() {
        listeners.forEach { it.onFavoritesChanged() }
    }

}
