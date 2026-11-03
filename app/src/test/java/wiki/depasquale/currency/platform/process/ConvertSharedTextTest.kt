package wiki.depasquale.currency.platform.process

import android.Manifest
import android.app.Activity
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcel
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
import org.robolectric.shadows.ShadowToast
import wiki.depasquale.currency.App
import wiki.depasquale.currency.R
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35], qualifiers = "en-rUS")
class ConvertSharedTextTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        shadowOf(app).grantPermissions(Manifest.permission.HIDE_OVERLAY_WINDOWS)
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(0)),
            ExchangeRate(USD, 1.25, Date(0)),
            ExchangeRate(JPY, 150.05, Date(0)),
            ExchangeRate(GBP, 0.8, Date(0)),
        )
    }

    @Test
    fun share_sheet_offers_the_exchange_rate_activity_for_plain_text() {
        val intent = Intent(Intent.ACTION_SEND).setType("text/plain")

        val match = app.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .single { it.activityInfo.packageName == app.packageName }

        assertEquals(ConvertTextActivity::class.java.name, match.activityInfo.name)
        assertEquals("Show exchange rate", match.loadLabel(app.packageManager).toString())
    }

    @Test
    fun shared_amount_is_shown_in_every_favorite() {
        favorites(EUR, JPY, GBP)

        share("12 USD").use {
            awaitText("€9.60")
            compose.onNodeWithText("¥1,440").assertExists()
            compose.onNodeWithText("£7.68").assertExists()
        }
    }

    @Test
    fun tapped_value_from_shared_text_is_copied_to_clipboard_and_never_returned_to_the_sharing_app() {
        favorites(EUR, GBP)

        share("12 USD").use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
            assertEquals("£7.68", clipboard().primaryClip?.getItemAt(0)?.text?.toString())
            assertEquals(0, ShadowToast.shownToastCount())
        }
    }

    @Test
    @Config(sdk = [32])
    fun copy_from_shared_text_is_confirmed_where_the_system_does_not_confirm_it() {
        favorites(EUR, GBP)

        share("12 USD").use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
            assertEquals("£7.68", clipboard().primaryClip?.getItemAt(0)?.text?.toString())
            assertEquals(app.getString(R.string.convert_text_copied), ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun shared_text_marked_editable_is_still_only_copied() {
        favorites(EUR, GBP)
        val intent = shareIntent("12 USD").putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()

            assertNull(scenario.result.resultData)
            assertEquals("£7.68", clipboard().primaryClip?.getItemAt(0)?.text?.toString())
        }
    }

    @Test
    fun shared_text_without_an_amount_and_currency_shows_error_with_way_into_app() {
        favorites(EUR)

        share("twelve apples").use {
            awaitText(app.getString(R.string.convert_text_unparseable))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    fun open_app_action_from_shared_text_starts_main_screen_without_result() {
        share("twelve apples").use { scenario ->
            awaitText(app.getString(R.string.convert_text_open_app))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
            assertEquals(
                MainActivity::class.java.name,
                shadowOf(app).nextStartedActivity?.component?.className
            )
        }
    }

    @Test
    fun very_long_shared_text_shows_error_instead_of_crashing() {
        favorites(EUR)

        share("12 USD " + "x".repeat(1_000_000)).use {
            awaitText(app.getString(R.string.convert_text_unparseable))
        }
    }

    @Test
    fun shared_stream_without_text_shows_error_with_way_into_app() {
        favorites(EUR)
        val intent = Intent(Intent.ACTION_SEND)
            .setClass(app, ConvertTextActivity::class.java)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_STREAM, Uri.parse("content://elsewhere/12-usd.txt"))

        ActivityScenario.launchActivityForResult<Activity>(intent).use {
            awaitText(app.getString(R.string.convert_text_unparseable))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    @Config(sdk = [32])
    fun share_whose_extras_cannot_be_read_closes_without_result() {
        favorites(EUR)
        val intent = parcelled(shareIntent("12 USD").putExtra("hostile", ConvertTextActivityTest.Unreadable()))

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
        }
    }

    @Test
    fun shared_text_dialog_ignores_touches_that_pass_through_an_overlay() {
        share("12 USD").use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.window.decorView.filterTouchesWhenObscured)
            }
        }
    }

    private fun shareIntent(text: String) = Intent(Intent.ACTION_SEND)
        .setClass(app, ConvertTextActivity::class.java)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, text)

    private fun share(text: String): ActivityScenario<Activity> =
        ActivityScenario.launchActivityForResult(shareIntent(text))

    private fun parcelled(intent: Intent): Intent {
        val parcel = Parcel.obtain()
        try {
            intent.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            return Intent.CREATOR.createFromParcel(parcel)
        } finally {
            parcel.recycle()
        }
    }

    private fun clipboard() = app.getSystemService(ClipboardManager::class.java)

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private fun awaitText(text: String) {
        compose.waitUntil(TIMEOUT_MS) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
    }

    private companion object {
        const val TIMEOUT_MS = 5_000L
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val JPY: Currency = Currency.getInstance("JPY")
        val GBP: Currency = Currency.getInstance("GBP")
    }

}
