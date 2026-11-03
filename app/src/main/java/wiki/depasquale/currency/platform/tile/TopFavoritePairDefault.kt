package wiki.depasquale.currency.platform.tile

import cursola.rate.FavoriteDataSource
import wiki.depasquale.currency.platform.link.CurrencyPair

class TopFavoritePairDefault(
    private val favorites: FavoriteDataSource
) : TopFavoritePair {

    override suspend fun current(): CurrencyPair? {
        val list = favorites.list()
        if (list.size < 2) return null
        return CurrencyPair(list[0], list[1])
    }

}
