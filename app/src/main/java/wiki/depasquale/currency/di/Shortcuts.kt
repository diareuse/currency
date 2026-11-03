package wiki.depasquale.currency.di

import android.content.Context
import cursola.rate.FavoriteDataSource
import cursola.rate.di.ExchangeRateLibrary
import kotlinx.coroutines.Dispatchers
import spike.Include
import spike.Singleton
import wiki.depasquale.currency.platform.FavoritesChangedListener
import wiki.depasquale.currency.platform.FavoritesChangedListenerComposite
import wiki.depasquale.currency.platform.FavoritesChangedListenerErrorBoundary
import wiki.depasquale.currency.platform.FavoritesChangedListenerOnContext
import wiki.depasquale.currency.platform.RatesUpdatedListener
import wiki.depasquale.currency.platform.RatesUpdatedListenerComposite
import wiki.depasquale.currency.platform.RatesUpdatedListenerErrorBoundary
import wiki.depasquale.currency.platform.link.ConverterLaunchIntent
import wiki.depasquale.currency.platform.link.ConverterLaunchIntentActivity
import wiki.depasquale.currency.platform.link.LaunchDestinationParser
import wiki.depasquale.currency.platform.link.LaunchDestinationParserCurrencyLink
import wiki.depasquale.currency.platform.shortcut.FavoritePairShortcuts
import wiki.depasquale.currency.platform.shortcut.PinnedShortcuts
import wiki.depasquale.currency.platform.shortcut.PinnedShortcutsCompat
import wiki.depasquale.currency.platform.shortcut.ShortcutCapacity
import wiki.depasquale.currency.platform.shortcut.ShortcutCapacityCompat
import wiki.depasquale.currency.platform.shortcut.ShortcutPublisher
import wiki.depasquale.currency.platform.shortcut.ShortcutPublisherCompat
import wiki.depasquale.currency.platform.shortcut.ShortcutRefresh
import wiki.depasquale.currency.platform.shortcut.ShortcutRefreshSerialized
import wiki.depasquale.currency.platform.tile.TileListeningRequest
import wiki.depasquale.currency.screen.MainActivity

private const val STATIC_SHORTCUTS = 2

@Include
internal fun shortcutPublisher(context: Context, launchIntent: ConverterLaunchIntent): ShortcutPublisher =
    ShortcutPublisherCompat(context, launchIntent)

@Include
internal fun shortcutCapacity(context: Context): ShortcutCapacity = ShortcutCapacityCompat(context, STATIC_SHORTCUTS)

@Include
internal fun pinnedShortcuts(context: Context): PinnedShortcuts = PinnedShortcutsCompat(context)

@Singleton
@Include
internal fun shortcutRefresh(
    @ExchangeRateLibrary favorite: FavoriteDataSource,
    capacity: ShortcutCapacity,
    publisher: ShortcutPublisher,
    pinned: PinnedShortcuts
): ShortcutRefresh = ShortcutRefreshSerialized(FavoritePairShortcuts(favorite, capacity, publisher, pinned))

@Include
internal fun ratesUpdatedListener(
    shortcuts: ShortcutRefresh,
    tile: TileListeningRequest
): RatesUpdatedListener = RatesUpdatedListenerComposite(
    listOf(
        RatesUpdatedListenerErrorBoundary(shortcuts::refresh),
        RatesUpdatedListenerErrorBoundary(tile),
    )
)

@Include
internal fun favoritesChangedListener(
    shortcuts: ShortcutRefresh,
    tile: TileListeningRequest
): FavoritesChangedListener = FavoritesChangedListenerComposite(
    listOf(
        FavoritesChangedListenerErrorBoundary(FavoritesChangedListenerOnContext(tile, Dispatchers.IO)),
        FavoritesChangedListenerErrorBoundary(FavoritesChangedListenerOnContext(shortcuts::refresh, Dispatchers.IO)),
    )
)

@Include
internal fun launchDestinations(): LaunchDestinationParser = LaunchDestinationParserCurrencyLink()

@Include
internal fun converterLaunchIntent(context: Context): ConverterLaunchIntent =
    ConverterLaunchIntentActivity(context, MainActivity::class.java)
