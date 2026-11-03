package wiki.depasquale.currency.platform.shortcut

import cursola.rate.FavoriteDataSource
import wiki.depasquale.currency.platform.link.CurrencyPair
import java.util.Currency

class FavoritePairShortcuts(
    private val favorites: FavoriteDataSource,
    private val capacity: ShortcutCapacity,
    private val publisher: ShortcutPublisher,
    private val pinned: PinnedShortcuts
) : ShortcutRefresh {

    override suspend fun refresh() {
        val list = favorites.list()
        pinned.keepOnly(everyPairOf(list))
        publisher.publish(firstAgainstOthers(list).take(capacity.dynamicSlots()))
    }

    private fun everyPairOf(list: List<Currency>): Set<CurrencyPair> =
        list.flatMap { from -> list.filter { it != from }.map { CurrencyPair(from, it) } }.toSet()

    private fun firstAgainstOthers(list: List<Currency>): List<CurrencyPair> {
        val first = list.firstOrNull() ?: return emptyList()
        return list.drop(1).map { CurrencyPair(first, it) }
    }

}
