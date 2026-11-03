package wiki.depasquale.currency.platform.tile

import android.content.ComponentName
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Looper
import android.service.quicksettings.Tile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowTileService
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity
import java.io.IOException
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@RunWith(AndroidJUnit4::class)
@Config(qualifiers = "en-rUS")
class QuickRateTileServiceTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint
    private val main get() = ComponentName(app, MainActivity::class.java)

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(0)),
            ExchangeRate(USD, 1.08423, Date(0)),
            ExchangeRate(CZK, 25.0, Date(0)),
        )
    }

    @Test
    fun listening_tile_shows_top_favorite_pair_with_its_rate_to_four_significant_digits() {
        favorites(EUR, USD, CZK)

        val tile = listen()

        assertEquals(Shown("EUR → USD", "1.084", Tile.STATE_ACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_keeps_four_significant_digits_for_rates_below_one() {
        favorites(CZK, EUR)

        val tile = listen()

        assertEquals(Shown("CZK → EUR", "0.04000", Tile.STATE_ACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_rounds_large_rates_to_four_significant_digits_with_grouping() {
        graph.exchangeRates.rates += ExchangeRate(IDR, 17_654.3, Date(0))
        favorites(EUR, IDR)

        val tile = listen()

        assertEquals(Shown("EUR → IDR", "17,650", Tile.STATE_ACTIVE), tile.shown())
    }

    @Test
    @Config(qualifiers = "cs-rCZ")
    fun listening_tile_formats_the_rate_in_the_device_locale() {
        favorites(EUR, USD)

        val tile = listen()

        assertEquals("1,084", tile.subtitle?.toString())
    }

    @Test
    fun listening_tile_without_favorites_asks_for_two_favorites() {
        val tile = listen()

        assertEquals(Shown("Exchange rate", "Add two favorites", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_with_a_single_favorite_asks_for_two_favorites() {
        favorites(EUR)

        val tile = listen()

        assertEquals(Shown("Exchange rate", "Add two favorites", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_reports_rates_unavailable_when_the_pair_has_no_rate() {
        favorites(EUR, GBP)

        val tile = listen()

        assertEquals(Shown("EUR → GBP", "Rates unavailable", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_reports_rates_unavailable_when_rate_lookup_fails() {
        favorites(EUR, USD)
        graph.exchangeRates.failure = IOException("offline")

        val tile = listen()

        assertEquals(Shown("EUR → USD", "Rates unavailable", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_reports_rates_unavailable_when_reading_favorites_fails() {
        favorites(EUR, USD)
        graph.favoriteReadFailure.value = SQLiteException("database is locked")

        val tile = listen()

        assertEquals(Shown("Exchange rate", "Rates unavailable", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    @Config(shadows = [FailingFirstUpdateTile::class])
    fun listening_tile_reports_rates_unavailable_when_updating_the_tile_fails() {
        favorites(EUR, USD)

        val tile = listen()

        assertEquals(Shown("EUR → USD", "Rates unavailable", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun listening_tile_gives_up_on_a_hung_rate_lookup_and_reports_rates_unavailable() {
        favorites(EUR, USD)
        graph.exchangeRates.hanging = true
        graph.quickRateDeadline.value = 100.milliseconds

        val tile = listen()

        assertEquals(Shown("EUR → USD", "Rates unavailable", Tile.STATE_INACTIVE), tile.shown())
    }

    @Test
    fun tile_that_stops_listening_abandons_its_rate_lookup() {
        favorites(EUR, USD)
        graph.exchangeRates.hanging = true
        graph.quickRateDeadline.value = 1.minutes
        val service = Robolectric.buildService(QuickRateTileService::class.java).create().get()
        service.onStartListening()
        await("rate lookup never started") { graph.exchangeRates.hung }

        service.onStopListening()

        await("rate lookup was never abandoned") { graph.exchangeRates.abandoned }
    }

    @Test
    @Config(sdk = [INTENT_COLLAPSE_SDK])
    fun tapping_tile_opens_converter_on_its_pair_in_a_fresh_task() {
        favorites(EUR, USD, CZK)
        val service = listeningService()

        service.onClick()

        val started = shadowOf(app).nextStartedActivity
        assertEquals(Opened(main, Intent.ACTION_VIEW, "currency://convert?from=EUR&to=USD"), started.opened())
        assertEquals(NEW_CLEARED_TASK, started.flags and NEW_CLEARED_TASK)
    }

    @Test
    @Config(sdk = [PENDING_INTENT_COLLAPSE_SDK], shadows = [PendingIntentTileService::class])
    fun tapping_tile_collapses_into_an_immutable_pending_intent_opening_converter_on_its_pair() {
        favorites(EUR, USD, CZK)
        val service = listeningService()

        service.onClick()

        val pending = Shadow.extract<PendingIntentTileService>(service).collapsedInto ?: fail("tile did not collapse")
        val opened = shadowOf(pending).savedIntent
        assertTrue(shadowOf(pending).isActivityIntent)
        assertTrue(pending.isImmutable)
        assertEquals(Opened(main, Intent.ACTION_VIEW, "currency://convert?from=EUR&to=USD"), opened.opened())
        assertEquals(NEW_CLEARED_TASK, opened.flags and NEW_CLEARED_TASK)
    }

    @Test
    @Config(sdk = [INTENT_COLLAPSE_SDK])
    fun tapping_tile_while_its_rate_is_still_loading_opens_converter_on_its_pair_without_waiting_for_the_rate() {
        favorites(EUR, USD)
        graph.exchangeRates.hanging = true
        graph.quickRateDeadline.value = 1.minutes
        val service = Robolectric.buildService(QuickRateTileService::class.java).create().get()
        service.onStartListening()
        await("rate lookup never started") { graph.exchangeRates.hung }

        service.onClick()

        var started: Intent? = null
        await("no activity started") { shadowOf(app).nextStartedActivity?.also { started = it } != null }
        assertEquals("currency://convert?from=EUR&to=USD", started?.dataString)
    }

    @Test
    @Config(sdk = [INTENT_COLLAPSE_SDK])
    fun tapping_tile_after_favorites_changed_opens_the_new_top_pair_while_its_rate_is_loading() {
        favorites(EUR, USD)
        val service = listeningService()
        runBlocking {
            graph.favorites.remove(USD)
            graph.favorites.add(CZK, 1)
        }
        graph.exchangeRates.hanging = true
        graph.quickRateDeadline.value = 1.minutes
        service.onStartListening()
        await("rate lookup never started") { graph.exchangeRates.hung }

        service.onClick()

        var started: Intent? = null
        await("no activity started") { shadowOf(app).nextStartedActivity?.also { started = it } != null }
        assertEquals("currency://convert?from=EUR&to=CZK", started?.dataString)
    }

    @Test
    @Config(sdk = [PENDING_INTENT_COLLAPSE_SDK], shadows = [RefusingCollapseTileService::class])
    fun tapping_tile_when_system_ui_refuses_to_open_the_app_does_not_crash() {
        favorites(EUR, USD)
        val service = listeningService()

        service.onClick()

        assertEquals(Shown("EUR → USD", "1.084", Tile.STATE_ACTIVE), service.qsTile.shown())
    }

    @Test
    @Config(sdk = [INTENT_COLLAPSE_SDK])
    fun tapping_tile_without_a_pair_opens_the_app_without_a_preselected_pair() {
        val service = listeningService()

        service.onClick()

        val started = shadowOf(app).nextStartedActivity ?: fail("no activity started")
        assertEquals(Opened(main, null, null), started.opened())
        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, started.flags and Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @Test
    @Config(sdk = [INTENT_COLLAPSE_SDK])
    fun tapping_tile_on_a_locked_device_asks_to_unlock_before_opening_converter() {
        favorites(EUR, USD)
        val service = listeningService()
        val lock = Shadow.extract<ShadowTileService>(service)
        lock.setLocked(true)

        service.onClick()

        val started = shadowOf(app).nextStartedActivity ?: fail("no activity started")
        assertFalse(service.isLocked)
        assertEquals("currency://convert?from=EUR&to=USD", started.dataString)
    }

    private fun listen(): Tile = listeningService().qsTile

    private fun listeningService(): QuickRateTileService {
        val service = Robolectric.buildService(QuickRateTileService::class.java).create().get()
        service.onStartListening()
        awaitRendered(service.qsTile)
        return service
    }

    private fun Intent.opened() = Opened(component, action, dataString)

    private data class Opened(val component: ComponentName?, val action: String?, val link: String?)

    private fun awaitRendered(tile: Tile): Tile {
        await("tile was never rendered") { tile.label != null }
        return tile
    }

    private fun await(failure: String, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + AWAIT_MS
        while (!condition()) {
            if (System.currentTimeMillis() > deadline) fail(failure)
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(POLL_MS)
        }
    }

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private fun Tile.shown() = Shown(label?.toString(), subtitle?.toString(), state)

    private data class Shown(val label: String?, val subtitle: String?, val state: Int)

    private companion object {
        const val AWAIT_MS = 5_000L
        const val POLL_MS = 10L
        const val INTENT_COLLAPSE_SDK = 32
        const val PENDING_INTENT_COLLAPSE_SDK = 35
        const val NEW_CLEARED_TASK = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val CZK: Currency = Currency.getInstance("CZK")
        val GBP: Currency = Currency.getInstance("GBP")
        val IDR: Currency = Currency.getInstance("IDR")
    }

}
