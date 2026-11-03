package wiki.depasquale.currency.platform.shortcut

import wiki.depasquale.currency.platform.link.CurrencyPair

fun interface ShortcutPublisher {

    fun publish(pairs: List<CurrencyPair>)

}
