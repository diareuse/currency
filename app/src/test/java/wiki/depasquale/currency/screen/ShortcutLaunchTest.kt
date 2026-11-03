package wiki.depasquale.currency.screen

import android.content.Intent
import android.os.Looper
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.net.toUri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35], qualifiers = "en-rUS")
class ShortcutLaunchTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(Currency.getInstance("EUR"), 1.0, Date(0)),
            ExchangeRate(Currency.getInstance("USD"), 2.0, Date(0)),
            ExchangeRate(Currency.getInstance("CZK"), 25.0, Date(0)),
        )
        runBlocking {
            graph.favorites.add(Currency.getInstance("EUR"), 3)
            graph.favorites.add(Currency.getInstance("USD"), 2)
            graph.favorites.add(Currency.getInstance("CZK"), 1)
        }
        graph.latest().value = "100"
    }

    @Test
    fun pair_shortcut_opens_converter_from_source_currency_with_target_listed_first() {
        launch("currency://convert?from=CZK&to=USD").use {
            awaitText(CZK_100_IN_USD)
            assertAbove(CZK_100_IN_USD, CZK_100_IN_EUR)
        }
    }

    @Test
    fun published_pair_shortcut_intent_opens_converter_with_its_pair() {
        val shortcut = ShortcutManagerCompat.getDynamicShortcuts(app).single { it.id == "convert-EUR-CZK" }

        ActivityScenario.launch<MainActivity>(shortcut.intent).use {
            awaitText(EUR_100_IN_USD)
            assertAbove(EUR_100_IN_CZK, EUR_100_IN_USD)
        }
    }

    @Test
    fun history_shortcut_opens_history_screen() {
        launch("currency://history").use {
            awaitText(HISTORY_TITLE)
        }
    }

    @Test
    @Config(qualifiers = "+w1000dp-h800dp")
    fun history_shortcut_on_wide_screen_opens_history_beside_converter() {
        launch("currency://history").use {
            awaitText(HISTORY_TITLE)
            awaitText(FAVORITES_HEADLINE)
        }
    }

    @Test
    fun source_picked_after_pair_shortcut_survives_visiting_history_and_coming_back() {
        launch("currency://convert?from=CZK&to=USD").use { scenario ->
            awaitText(CZK_100_IN_USD)
            compose.onNodeWithText(EURO).performClick()
            compose.onNodeWithContentDescription(SHOW_HISTORY).performClick()
            awaitText(HISTORY_TITLE)

            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }

            assertSettledText(EUR_100_IN_USD)
        }
    }

    @Test
    fun history_shortcut_is_not_reopened_when_activity_is_recreated() {
        launch("currency://history").use { scenario ->
            awaitText(HISTORY_TITLE)

            scenario.recreate()
            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }

            assertSettledText(FAVORITES_HEADLINE)
        }
    }

    @Test
    fun pair_link_replayed_from_recents_opens_normal_start_screen() {
        val replay = link("currency://convert?from=EUR&to=CZK").addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)

        assertStartScreen(ActivityScenario.launch(replay))
    }

    @Test
    fun pair_link_from_another_app_stores_neither_source_currency_nor_amount() {
        launch("currency://convert?from=CZK&to=USD").use {
            awaitText(CZK_100_IN_USD)

            settlePastAmountSaveDelay()

            assertEquals("EUR", graph.latest().currency)
            assertEquals("100", graph.latest().value)
        }
    }

    @Test
    fun currency_picked_after_pair_link_is_stored() {
        graph.latest().currency = "USD"

        launch("currency://convert?from=CZK&to=USD").use {
            awaitText(CZK_100_IN_USD)
            compose.onNodeWithText(EURO).performClick()

            settlePastAmountSaveDelay()

            assertEquals("EUR", graph.latest().currency)
        }
    }

    @Test
    fun amount_typed_after_pair_link_stores_amount_with_linked_source_currency() {
        launch("currency://convert?from=CZK&to=USD").use {
            awaitText(CZK_100_IN_USD)
            compose.onNode(hasSetTextAction()).performTextReplacement("250")

            settlePastAmountSaveDelay()

            assertEquals("CZK", graph.latest().currency)
            assertEquals("250", graph.latest().value)
        }
    }

    @Test
    fun picking_another_source_after_pair_link_stops_listing_linked_target_first() {
        launch("currency://convert?from=USD&to=CZK").use {
            awaitText(USD_100_IN_EUR)
            assertAbove(USD_100_IN_CZK, USD_100_IN_EUR)

            compose.onNodeWithText(EURO).performClick()

            awaitText(EUR_100_IN_USD)
            assertAbove(EUR_100_IN_USD, EUR_100_IN_CZK)
        }
    }

    @Test
    fun convert_shortcut_without_pair_opens_normal_start_screen() {
        assertStartScreen("currency://convert")
    }

    @Test
    fun pair_link_missing_target_opens_normal_start_screen() {
        assertStartScreen("currency://convert?from=USD")
    }

    @Test
    fun pair_link_with_unknown_code_opens_normal_start_screen() {
        assertStartScreen("currency://convert?from=ABC&to=CZK")
    }

    @Test
    fun pair_link_with_lowercase_code_opens_normal_start_screen() {
        assertStartScreen("currency://convert?from=usd&to=CZK")
    }

    @Test
    fun pair_link_with_very_long_code_opens_normal_start_screen() {
        assertStartScreen("currency://convert?from=USD&to=" + "C".repeat(100_000))
    }

    @Test
    fun pair_link_with_foreign_scheme_opens_normal_start_screen() {
        assertStartScreen("https://convert?from=EUR&to=CZK")
    }

    @Test
    fun pair_link_with_unknown_host_opens_normal_start_screen() {
        assertStartScreen("currency://transfer?from=EUR&to=CZK")
    }

    @Test
    fun malformed_link_opens_normal_start_screen() {
        assertStartScreen("currency://convert?from=EUR&to=CZK%zz #")
    }

    private fun assertStartScreen(link: String) = assertStartScreen(launch(link))

    private fun assertStartScreen(scenario: ActivityScenario<MainActivity>) {
        scenario.use {
            assertSettledText(EUR_100_IN_USD)
            compose.onNodeWithText(HISTORY_TITLE).assertDoesNotExist()
            assertAbove(EUR_100_IN_USD, EUR_100_IN_CZK)
        }
    }

    private fun launch(link: String) = ActivityScenario.launch<MainActivity>(link(link))

    private fun link(link: String) = Intent(Intent.ACTION_VIEW, link.toUri()).setClass(app, MainActivity::class.java)

    private fun settlePastAmountSaveDelay() {
        compose.waitForIdle()
        shadowOf(Looper.getMainLooper()).idleFor(AMOUNT_SAVE_DELAY)
        compose.waitForIdle()
    }

    private fun awaitText(text: String) {
        compose.waitUntil(TIMEOUT_MS) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
    }

    private fun assertSettledText(text: String) {
        awaitText(text)
        compose.waitForIdle()
        compose.onNodeWithText(text).assertExists()
    }

    private fun assertAbove(upper: String, lower: String) {
        compose.waitUntil(TIMEOUT_MS) { top(upper) < top(lower) }
        compose.waitForIdle()
        assertTrue(top(upper) < top(lower), "$upper is not above $lower")
    }

    private fun top(text: String) = compose.onNodeWithText(text).getBoundsInRoot().top

    private companion object {
        const val TIMEOUT_MS = 5_000L
        val AMOUNT_SAVE_DELAY: java.time.Duration = java.time.Duration.ofSeconds(10)
        const val USD_100_IN_EUR = "€50.00"
        const val USD_100_IN_CZK = "CZK1,250.00"
        const val CZK_100_IN_USD = "$8.00"
        const val CZK_100_IN_EUR = "€4.00"
        const val HISTORY_TITLE = "History"
        const val FAVORITES_HEADLINE = "Your favorites"
        const val SHOW_HISTORY = "Show history"
        const val EURO = "Euro"
        const val EUR_100_IN_USD = "$200.00"
        const val EUR_100_IN_CZK = "CZK2,500.00"
    }

}
