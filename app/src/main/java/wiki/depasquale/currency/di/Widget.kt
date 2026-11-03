package wiki.depasquale.currency.di

import android.content.Context
import cursola.rate.FavoriteDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.CurrencyConverterTimeout
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import spike.Include
import spike.Singleton
import wiki.depasquale.currency.platform.LatestValueChangedListener
import wiki.depasquale.currency.platform.LatestValueChangedListenerLaunching
import wiki.depasquale.currency.platform.widget.FavoritesSnapshotSource
import wiki.depasquale.currency.platform.widget.FavoritesSnapshotSourceErrorBoundary
import wiki.depasquale.currency.platform.widget.FavoritesSnapshotSourceLatest
import wiki.depasquale.currency.platform.widget.FavoritesSnapshotSourceOnContext
import wiki.depasquale.currency.platform.widget.FavoritesWidget
import wiki.depasquale.currency.platform.widget.FavoritesWidgetRevision
import wiki.depasquale.currency.platform.widget.WidgetUpdater
import wiki.depasquale.currency.platform.widget.WidgetUpdaterErrorBoundary
import wiki.depasquale.currency.platform.widget.WidgetUpdaterGlance
import wiki.depasquale.currency.platform.widget.WidgetUpdaterOnContext
import wiki.depasquale.currency.platform.widget.WidgetUpdaterRevision
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class FavoritesWidgetDeadline(val value: Duration)

@Include
internal fun favoritesWidgetDeadline() = FavoritesWidgetDeadline(2.seconds)

@Include
internal fun favoritesSnapshot(
    latest: LatestValueDataSource,
    favorite: FavoriteDataSource,
    converter: CurrencyConverter,
    deadline: FavoritesWidgetDeadline
): FavoritesSnapshotSource {
    val bounded = CurrencyConverterTimeout(converter, deadline.value)
    val core = FavoritesSnapshotSourceLatest(latest, favorite, bounded)
    val offMain = FavoritesSnapshotSourceOnContext(core, Dispatchers.IO)
    return FavoritesSnapshotSourceErrorBoundary(offMain)
}

// Spike alpha07 truncates guard tables whose last singleton id is 9; this singleton shifts the ids (see GuardTableTest).
internal data class WidgetSessions(val updater: WidgetUpdater)

@Singleton
@Include
internal fun widgetSessions(context: Context) =
    WidgetSessions(WidgetUpdaterOnContext(WidgetUpdaterGlance(context, FavoritesWidget()), Dispatchers.IO))

@Include
internal fun widgetUpdater(revision: FavoritesWidgetRevision, sessions: WidgetSessions): WidgetUpdater =
    WidgetUpdaterRevision(revision, sessions.updater)

internal data class WidgetUpdateScope(val value: CoroutineScope)

@Singleton
@Include
internal fun widgetUpdateScope() =
    // Last-resort guard only: updates launched here are already wrapped by WidgetUpdaterErrorBoundary.
    WidgetUpdateScope(CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }))

@Include
internal fun latestValueChangedListener(widget: WidgetUpdater, scope: WidgetUpdateScope): LatestValueChangedListener =
    LatestValueChangedListenerLaunching(scope.value, WidgetUpdaterErrorBoundary(widget)::update)
