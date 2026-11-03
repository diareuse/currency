package wiki.depasquale.currency.platform.process

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
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
import org.robolectric.shadows.ShadowDialog
import wiki.depasquale.currency.App
import wiki.depasquale.currency.R
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.favorite.FavoriteItemTestTag
import java.io.IOException
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35], qualifiers = "en-rUS")
class ConvertTextSheetTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint
    private val bottomSheet = SemanticsMatcher.expectValue(
        SemanticsProperties.PaneTitle,
        app.getString(androidx.compose.material3.R.string.m3c_bottom_sheet_pane_title)
    )

    @Before
    fun setUp() {
        shadowOf(app).grantPermissions(Manifest.permission.HIDE_OVERLAY_WINDOWS)
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(0)),
            ExchangeRate(USD, 1.25, Date(0)),
            ExchangeRate(GBP, 0.8, Date(0)),
        )
    }

    @Test
    fun converted_rows_are_shown_inside_a_bottom_sheet() {
        favorites(EUR, GBP)

        launch("12 USD").use {
            awaitText("£7.68")

            compose.onNode(hasText("£7.68") and hasAnyAncestor(bottomSheet)).assertExists()
        }
    }

    @Test
    fun converted_row_is_the_home_screen_favorite_row_with_name_code_and_value() {
        favorites(EUR, GBP)

        launch("12 USD").use {
            awaitText("£7.68")

            compose.onNode(
                hasTestTag(FavoriteItemTestTag) and hasText("British Pound") and hasText("GBP") and hasText("£7.68")
            ).assertExists()
        }
    }

    @Test
    fun unavailable_rates_and_way_into_app_are_shown_inside_the_bottom_sheet() {
        favorites(EUR, GBP)
        graph.exchangeRates.failure = IOException("offline")

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_unavailable))

            compose.onNode(hasText(app.getString(R.string.convert_text_unavailable)) and hasAnyAncestor(bottomSheet))
                .assertExists()
            compose.onNode(hasText(app.getString(R.string.convert_text_open_app)) and hasAnyAncestor(bottomSheet))
                .assertExists()
        }
    }

    @Test
    fun tapping_outside_the_sheet_closes_it_without_result() {
        favorites(EUR, GBP)

        launch("12 USD").use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithContentDescription(app.getString(androidx.compose.ui.R.string.close_sheet)).performTouchInput { click(Offset(centerX, 1f)) }

            assertClosedWithoutResult(scenario)
        }
    }

    @Test
    fun swiping_the_sheet_down_closes_it_without_result() {
        favorites(EUR, GBP)

        launch("12 USD").use { scenario ->
            awaitText("£7.68")
            compose.onNode(bottomSheet).performTouchInput { swipeDown() }

            assertClosedWithoutResult(scenario)
        }
    }

    @Test
    fun pressing_back_closes_the_sheet_without_result() {
        favorites(EUR, GBP)

        launch("12 USD").use { scenario ->
            awaitText("£7.68")
            val sheet = ShadowDialog.getLatestDialog()
            sheet.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
            sheet.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))

            assertClosedWithoutResult(scenario)
        }
    }

    @Test
    fun pressing_back_closes_the_shared_text_sheet_without_result() {
        favorites(EUR, GBP)
        val intent = Intent(Intent.ACTION_SEND)
            .setClass(app, ConvertTextActivity::class.java)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, "12 USD")

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            awaitText("£7.68")
            val sheet = ShadowDialog.getLatestDialog()
            sheet.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
            sheet.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))

            assertClosedWithoutResult(scenario)
        }
    }

    @Test
    fun sheet_is_shown_over_a_transparent_full_screen_window_without_a_dialog_frame() {
        launch("12 USD").use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(Color.TRANSPARENT, (activity.window.decorView.background as? ColorDrawable)?.color)
                assertFalse(activity.window.isFloating)
                assertEquals(0, activity.window.attributes.windowAnimations)
            }
        }
    }

    @Test
    fun sheet_ignores_touches_that_pass_through_an_overlay() {
        favorites(EUR, GBP)

        launch("12 USD").use {
            awaitText("£7.68")

            assertTrue(ShadowDialog.getLatestDialog().window!!.decorView.filterTouchesWhenObscured)
        }
    }

    @Test
    @Config(sdk = [34])
    fun screen_draws_edge_to_edge_behind_transparent_system_bars_where_the_system_does_not_enforce_it() {
        launch("12 USD").use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(Color.TRANSPARENT, activity.window.statusBarColor)
                assertEquals(Color.TRANSPARENT, activity.window.navigationBarColor)
            }
        }
    }

    private fun assertClosedWithoutResult(scenario: ActivityScenario<Activity>) {
        compose.mainClock.advanceTimeUntil(TIMEOUT_MS) { scenario.isFinishing() }
        assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
        assertNull(scenario.result.resultData)
    }

    private fun ActivityScenario<Activity>.isFinishing(): Boolean {
        var finishing = false
        onActivity { finishing = it.isFinishing }
        return finishing
    }

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private fun launch(text: String): ActivityScenario<Activity> = ActivityScenario.launchActivityForResult(
        Intent(Intent.ACTION_PROCESS_TEXT)
            .setClass(app, ConvertTextActivity::class.java)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_PROCESS_TEXT, text)
    )

    private fun awaitText(text: String) {
        compose.waitUntil(TIMEOUT_MS) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
    }

    private companion object {
        const val TIMEOUT_MS = 5_000L
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val GBP: Currency = Currency.getInstance("GBP")
    }

}
