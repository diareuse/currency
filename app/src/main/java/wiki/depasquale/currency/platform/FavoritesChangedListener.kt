package wiki.depasquale.currency.platform

fun interface FavoritesChangedListener {

    suspend fun onFavoritesChanged()

}
