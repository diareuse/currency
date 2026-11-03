package wiki.depasquale.currency.platform.tile

import wiki.depasquale.currency.platform.link.CurrencyPair

fun interface TopFavoritePair {

    suspend fun current(): CurrencyPair?

}
