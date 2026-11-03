package wiki.depasquale.currency.platform.shortcut

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ShortcutManager
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.platform.UpdateWorker
import wiki.depasquale.currency.screen.MainActivity
import java.io.IOException
import java.util.Currency
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class FavoritePairShortcutsTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Test
    fun successful_rate_update_publishes_first_favorite_against_each_other_favorite_opening_main_activity_in_a_fresh_task() {
        favorites("EUR", "USD", "CZK")
        ShortcutManagerCompat.removeAllDynamicShortcuts(app)

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(
            listOf(
                Published("convert-EUR-USD", "EUR → USD", "currency://convert?from=EUR&to=USD"),
                Published("convert-EUR-CZK", "EUR → CZK", "currency://convert?from=EUR&to=CZK"),
            ),
            published()
        )
        ShortcutManagerCompat.getDynamicShortcuts(app).forEach {
            assertEquals(Intent.ACTION_VIEW, it.intent.action)
            assertEquals(ComponentName(app, MainActivity::class.java), it.intent.component)
            assertEquals(NEW_CLEARED_TASK, it.intent.flags and NEW_CLEARED_TASK)
        }
    }

    @Test
    fun successful_rate_update_drops_shortcuts_of_pairs_that_are_no_longer_favorites() {
        favorites("EUR", "CZK")
        ShortcutManagerCompat.setDynamicShortcuts(app, listOf(staleShortcut("convert-EUR-GBP")))

        runUpdate()

        assertEquals(listOf("convert-EUR-CZK"), published().map { it.id })
    }

    @Test
    fun failed_rate_update_leaves_published_shortcuts_untouched() {
        favorites("EUR", "USD", "CZK")
        val before = published()
        shrinkCapacitySoAnyRepublishWouldBeVisible()
        graph.exchangeRates.failure = IOException("offline")

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.retry(), result)
        assertEquals(before, published())
    }

    @Test
    fun published_pairs_fill_only_the_slots_left_beside_the_two_static_shortcuts() {
        favorites("EUR", "USD", "CZK", "GBP", "JPY")
        shadowOf(app.getSystemService(ShortcutManager::class.java)).setMaxShortcutCountPerActivity(4)

        runUpdate()

        assertEquals(listOf("convert-EUR-USD", "convert-EUR-CZK"), published().map { it.id })
    }

    @Test
    fun empty_rate_update_leaves_published_shortcuts_untouched() {
        favorites("EUR", "USD", "CZK")
        val before = published()
        shrinkCapacitySoAnyRepublishWouldBeVisible()
        graph.exchangeRates.rates = emptyList()

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.retry(), result)
        assertEquals(before, published())
    }

    @Test
    fun no_pair_shortcuts_are_published_when_static_shortcuts_take_every_slot() {
        favorites("EUR", "USD")
        shadowOf(app.getSystemService(ShortcutManager::class.java)).setMaxShortcutCountPerActivity(1)

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(emptyList(), published())
    }

    @Test
    fun single_favorite_publishes_no_shortcuts() {
        favorites("EUR")

        runUpdate()

        assertEquals(emptyList(), published())
    }

    @Test
    fun adding_and_removing_favorites_republishes_shortcuts_without_waiting_for_a_rate_update() {
        favorites("EUR", "USD")
        val afterAdd = published().map { it.id }

        runBlocking { graph.favorites.remove(Currency.getInstance("USD")) }

        assertEquals(listOf("convert-EUR-USD"), afterAdd)
        assertEquals(emptyList(), published())
    }

    @Test
    @Config(shadows = [RejectingShortcutManager::class])
    fun launcher_rejecting_shortcuts_neither_breaks_favorite_edits_nor_fails_the_rate_update() {
        favorites("EUR", "USD")

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(listOf("EUR", "USD"), runBlocking { graph.favorites.list().map { it.currencyCode } })
    }

    @Test
    @Config(shadows = [RecordingShortcutManager::class])
    fun rate_update_leaves_launcher_untouched_when_the_pair_set_is_unchanged() {
        favorites("EUR", "USD", "CZK")
        val publishesBefore = recorder().dynamicPublishes

        runUpdate()

        assertEquals(publishesBefore, recorder().dynamicPublishes)
        assertEquals(listOf("convert-EUR-USD", "convert-EUR-CZK"), published().map { it.id })
    }

    @Test
    @Config(sdk = [PINNED_LOOKUP_SHADOWED_SDK], shadows = [RecordingShortcutManager::class])
    fun removing_a_favorite_disables_the_pinned_shortcut_of_its_pair_with_an_explanation() {
        favorites("EUR", "USD", "CZK")
        pin("convert-EUR-USD")

        runBlocking { graph.favorites.remove(Currency.getInstance("USD")) }

        assertEquals(listOf("convert-EUR-USD" to "Currency removed from favorites"), recorder().disabled)
    }

    @Test
    @Config(sdk = [PINNED_LOOKUP_SHADOWED_SDK], shadows = [RecordingShortcutManager::class])
    fun adding_the_favorite_back_enables_its_pinned_shortcut_again() {
        favorites("EUR", "USD", "CZK")
        pin("convert-EUR-USD")
        runBlocking { graph.favorites.remove(Currency.getInstance("USD")) }

        runBlocking { graph.favorites.add(Currency.getInstance("USD"), 0) }

        assertEquals(listOf("convert-EUR-USD"), recorder().enabled.distinct())
    }

    @Test
    @Config(sdk = [PINNED_LOOKUP_SHADOWED_SDK], shadows = [RecordingShortcutManager::class])
    fun pinned_pair_stays_enabled_while_both_currencies_are_favorites_even_if_neither_is_first() {
        favorites("EUR", "USD", "CZK")
        pin("convert-EUR-USD")
        runBlocking { graph.favorites.remove(Currency.getInstance("EUR")) }
        recorder().disabled.clear()

        runBlocking { graph.favorites.add(Currency.getInstance("EUR"), 0) }

        assertEquals(emptyList(), recorder().disabled)
        assertEquals(listOf("convert-EUR-USD"), recorder().enabled.distinct())
    }

    @Test
    @Config(sdk = [PINNED_LOOKUP_SHADOWED_SDK], shadows = [RecordingShortcutManager::class])
    fun pinned_shortcut_is_enabled_before_its_pair_is_published_again() {
        favorites("EUR", "USD", "CZK")
        pin("convert-EUR-USD")
        runBlocking { graph.favorites.remove(Currency.getInstance("USD")) }
        recorder().events.clear()

        runBlocking { graph.favorites.add(Currency.getInstance("USD"), 0) }

        assertEquals(
            listOf("enable convert-EUR-USD", "publish convert-EUR-CZK, convert-EUR-USD"),
            recorder().events
        )
    }

    private fun favorites(vararg codes: String) = runBlocking {
        codes.forEachIndexed { index, code -> graph.favorites.add(Currency.getInstance(code), codes.size - index) }
    }

    private fun runUpdate() = runBlocking {
        TestListenableWorkerBuilder<UpdateWorker>(app)
            .setWorkerFactory(graph.workerFactory)
            .build()
            .doWork()
    }

    private fun shrinkCapacitySoAnyRepublishWouldBeVisible() {
        shadowOf(app.getSystemService(ShortcutManager::class.java)).setMaxShortcutCountPerActivity(3)
    }

    private fun staleShortcut(id: String) = ShortcutInfoCompat.Builder(app, id)
        .setShortLabel(id)
        .setIntent(Intent(Intent.ACTION_VIEW).setClass(app, MainActivity::class.java))
        .build()

    private fun recorder() =
        Shadow.extract<RecordingShortcutManager>(app.getSystemService(ShortcutManager::class.java))

    private fun pin(id: String) {
        val shortcut = ShortcutManagerCompat.getDynamicShortcuts(app).single { it.id == id }
        ShortcutManagerCompat.requestPinShortcut(app, shortcut, null)
    }

    private fun published() = ShortcutManagerCompat.getDynamicShortcuts(app)
        .sortedBy(ShortcutInfoCompat::getRank)
        .map { Published(it.id, it.shortLabel.toString(), it.intent.dataString) }

    private companion object {
        const val PINNED_LOOKUP_SHADOWED_SDK = 29
        const val NEW_CLEARED_TASK = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private data class Published(val id: String, val label: String, val link: String?)

}
