package wiki.depasquale.currency.platform.shortcut

import wiki.depasquale.currency.platform.link.CurrencyPair

fun interface PinnedShortcuts {

    fun keepOnly(pairs: Set<CurrencyPair>)

}
