package wiki.depasquale.currency.platform.widget

import cursola.rate.convert.Conversion
import cursola.rate.convert.Money

sealed interface FavoritesSnapshot {

    data class NoFavorites(val source: Money) : FavoritesSnapshot

    data class Unavailable(val source: Money?) : FavoritesSnapshot

    data class Converted(val source: Money, val conversions: List<Conversion>) : FavoritesSnapshot

}
