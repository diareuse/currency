package wiki.depasquale.currency.platform

import cursola.rate.FavoriteDataSource
import java.util.Currency

class FavoriteDataSourceNotifying(
    private val origin: FavoriteDataSource,
    private val listener: FavoritesChangedListener
) : FavoriteDataSource by origin {

    override suspend fun add(currency: Currency, priority: Int) {
        origin.add(currency, priority)
        listener.onFavoritesChanged()
    }

    override suspend fun remove(currency: Currency) {
        origin.remove(currency)
        listener.onFavoritesChanged()
    }

}
