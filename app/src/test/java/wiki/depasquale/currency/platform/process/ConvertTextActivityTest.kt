package wiki.depasquale.currency.platform.process

import android.Manifest
import android.app.Activity
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.BadParcelableException
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.test.assertCountEquals
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
import org.robolectric.util.ReflectionHelpers
import wiki.depasquale.currency.App
import wiki.depasquale.currency.R
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35], qualifiers = "en-rUS")
class ConvertTextActivityTest {

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
    fun process_text_selection_menu_offers_convert_activity_for_plain_text() {
        val intent = Intent(Intent.ACTION_PROCESS_TEXT).setType("text/plain")

        val matches = app.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .filter { it.activityInfo.packageName == app.packageName }

        assertEquals(listOf(ConvertTextActivity::class.java.name), matches.map { it.activityInfo.name })
        assertEquals(true, matches.single().activityInfo.exported)
    }

    @Test
    fun process_text_selection_menu_labels_the_convert_activity_distinctly_from_the_system_convert_action() {
        val intent = Intent(Intent.ACTION_PROCESS_TEXT).setType("text/plain")

        val match = app.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .single { it.activityInfo.packageName == app.packageName }

        assertEquals("Show exchange rate", match.loadLabel(app.packageManager).toString())
    }

    @Test
    fun selected_code_amount_is_shown_in_every_favorite_rounded_to_its_fraction_digits_and_tapped_one_replaces_selection() {
        favorites(EUR, JPY, GBP)

        launch("12 USD").use { scenario ->
            awaitText("€9.60")
            compose.onNodeWithText("¥1,440").assertExists()
            compose.onNodeWithText("£7.68").performClick()

            assertReplacedWith("£7.68", scenario)
        }
    }

    @Test
    fun selected_symbol_amount_skips_its_own_currency_among_favorites_and_tapped_one_replaces_selection() {
        favorites(EUR, JPY, GBP)

        launch("€30").use { scenario ->
            awaitText("¥4,502")
            compose.onNodeWithText("£24.00").assertExists()
            compose.onAllNodesWithText("€", substring = true).assertCountEquals(0)
            compose.onNodeWithText("¥4,502").performClick()

            assertReplacedWith("¥4,502", scenario)
        }
    }

    @Test
    fun selected_grouped_amount_with_lowercase_code_is_converted_and_tapped_one_replaces_selection() {
        favorites(JPY, GBP)

        launch("1,234.5 eur").use { scenario ->
            awaitText("£987.60")
            compose.onNodeWithText("¥185,237").assertExists()
            compose.onNodeWithText("£987.60").performClick()

            assertReplacedWith("£987.60", scenario)
        }
    }

    @Test
    fun tapped_value_from_read_only_selection_is_copied_to_clipboard_without_replacing_selection() {
        favorites(EUR, GBP)
        val intent = intent("12 USD").putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData?.getStringExtra(Intent.EXTRA_PROCESS_TEXT))
            assertEquals("£7.68", clipboard().primaryClip?.getItemAt(0)?.text?.toString())
            assertEquals(0, ShadowToast.shownToastCount())
        }
    }

    @Test
    @Config(sdk = [32])
    fun copy_from_read_only_selection_is_confirmed_where_the_system_does_not_confirm_it() {
        favorites(EUR, GBP)
        val intent = intent("12 USD").putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertEquals("£7.68", clipboard().primaryClip?.getItemAt(0)?.text?.toString())
            assertEquals(app.getString(R.string.convert_text_copied), ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun selection_without_an_amount_and_currency_shows_error_with_way_into_app() {
        favorites(EUR)

        launch("twelve apples").use {
            awaitText(app.getString(R.string.convert_text_unparseable))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    fun open_app_action_starts_main_screen_and_leaves_selection_untouched() {
        launch("twelve apples").use { scenario ->
            awaitText(app.getString(R.string.convert_text_open_app))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).performClick()

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertEquals(
                MainActivity::class.java.name,
                shadowOf(app).nextStartedActivity?.component?.className
            )
        }
    }

    @Test
    fun selection_without_favorites_shows_hint_with_way_into_app() {
        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_no_favorites))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    fun selection_in_the_only_favorite_currency_shows_hint_with_way_into_app() {
        favorites(USD)

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_no_favorites))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    fun failing_rate_lookup_shows_error_instead_of_crashing() {
        favorites(EUR, GBP)
        graph.exchangeRates.failure = IOException("offline")

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_unavailable))
        }
    }

    @Test
    fun missing_rates_for_every_favorite_show_error() {
        favorites(EUR, GBP)
        graph.exchangeRates.rates = listOf(ExchangeRate(EUR, 1.0, Date(0)))

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_unavailable))
        }
    }

    @Test
    fun explicit_launch_with_an_action_other_than_process_text_closes_without_result() {
        favorites(EUR, GBP)
        val intent = intent("12 USD").setAction(Intent.ACTION_VIEW)

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
        }
    }

    @Test
    fun conversion_in_progress_is_shown_while_rates_load() {
        favorites(EUR, GBP)
        graph.exchangeRates.hanging = true

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_loading))
        }
    }

    @Test
    fun favorite_without_minor_units_like_gold_shows_four_significant_digits() {
        graph.exchangeRates.rates += ExchangeRate(XAU, 0.0004, Date(0))
        favorites(XAU)

        launch("12 USD").use { scenario ->
            awaitText("XAU0.00384")
            compose.onNodeWithText("XAU0.00384").performClick()

            assertReplacedWith("XAU0.00384", scenario)
        }
    }

    @Test
    fun large_amount_in_currency_without_minor_units_keeps_its_minor_units() {
        graph.exchangeRates.rates += ExchangeRate(XAU, 0.0004, Date(0))
        favorites(XAU)

        launch("12,345,678 USD").use {
            awaitText("XAU3,950.62")
        }
    }

    @Test
    fun recreated_dialog_keeps_converted_rows_without_querying_rates_again() {
        favorites(EUR, GBP)

        launch("12 USD").use { scenario ->
            awaitText("£7.68")
            graph.exchangeRates.failure = IOException("offline")
            scenario.recreate()

            awaitText("£7.68")
            compose.onNodeWithText("£7.68").performClick()
            assertReplacedWith("£7.68", scenario)
        }
    }

    @Test
    fun rates_that_never_arrive_end_in_error_instead_of_endless_progress() {
        favorites(EUR, GBP)
        graph.exchangeRates.hanging = true
        graph.textConversionDeadline.value = 200.milliseconds

        launch("12 USD").use {
            awaitText(app.getString(R.string.convert_text_unavailable))
        }
    }

    @Test
    fun exact_half_minor_unit_rounds_up() {
        graph.exchangeRates.rates += ExchangeRate(KRW, 2.5, Date(0))
        favorites(KRW)

        launch("€1").use {
            awaitText("₩3")
        }
    }

    @Test
    fun rows_name_each_favorite_in_favorite_priority_order() {
        favorites(GBP, EUR, JPY)

        launch("12 USD").use {
            awaitText("British Pound")
            val tops = listOf("British Pound", "Euro", "Japanese Yen").map { top(it) }

            assertEquals(tops.sorted(), tops)
        }
    }

    @Test
    fun process_text_request_without_selected_text_shows_error_with_way_into_app() {
        favorites(EUR)
        val intent = intent("12 USD").apply { removeExtra(Intent.EXTRA_PROCESS_TEXT) }

        ActivityScenario.launchActivityForResult<Activity>(intent).use {
            awaitText(app.getString(R.string.convert_text_unparseable))
            compose.onNodeWithText(app.getString(R.string.convert_text_open_app)).assertExists()
        }
    }

    @Test
    fun dialog_ignores_touches_that_pass_through_an_overlay() {
        launch("12 USD").use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.window.decorView.filterTouchesWhenObscured)
            }
        }
    }

    @Test
    fun dialog_hides_other_apps_overlay_windows_while_shown() {
        val requested = app.packageManager.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions.orEmpty()
        assertTrue(Manifest.permission.HIDE_OVERLAY_WINDOWS in requested)

        launch("12 USD").use { scenario ->
            scenario.onActivity { activity ->
                val privateFlags = ReflectionHelpers.getField<Int>(activity.window.attributes, "privateFlags")

                assertEquals(HIDE_NON_SYSTEM_OVERLAY_WINDOWS, privateFlags and HIDE_NON_SYSTEM_OVERLAY_WINDOWS)
            }
        }
    }

    @Test
    @Config(qualifiers = "de-rDE")
    fun amounts_follow_the_device_locale_format() {
        favorites(EUR, JPY)

        launch("12 USD").use { scenario ->
            awaitText("9,60\u00A0€")
            compose.onNodeWithText("1.440\u00A0¥").assertExists()
            compose.onNodeWithText("9,60\u00A0€").performClick()

            assertReplacedWith("9,60\u00A0€", scenario)
        }
    }

    @Test
    @Config(sdk = [32])
    fun request_whose_extras_cannot_be_read_closes_without_result() {
        favorites(EUR)
        val intent = parcelled(intent("12 USD").putExtra("hostile", Unreadable()))

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
        }
    }

    @Test
    @Config(sdk = [32])
    fun request_with_serializable_extra_of_unknown_class_closes_without_result() {
        favorites(EUR)
        val intent = parcelled(intent("12 USD").putExtra("hostile", UnknownSerializable()))

        ActivityScenario.launchActivityForResult<Activity>(intent).use { scenario ->
            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
            assertNull(scenario.result.resultData)
        }
    }

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

    class UnknownSerializable : Serializable {

        @Suppress("UnusedPrivateMember", "UnusedParameter")
        private fun readObject(input: ObjectInputStream): Unit = throw ClassNotFoundException("from another app")

    }

    class Unreadable : Parcelable {

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = Unit

        companion object CREATOR : Parcelable.Creator<Unreadable> {
            override fun createFromParcel(source: Parcel): Unreadable =
                throw BadParcelableException("unknown class from another app")

            override fun newArray(size: Int) = arrayOfNulls<Unreadable>(size)
        }

    }

    @Test
    fun inexact_conversion_is_rounded_to_the_currency_minor_unit() {
        graph.exchangeRates.rates = graph.exchangeRates.rates.map {
            if (it.currency == GBP) ExchangeRate(GBP, 0.85123, Date(0)) else it
        }
        favorites(GBP)

        launch("12 USD").use { scenario ->
            awaitText("£8.17")
            compose.onNodeWithText("£8.17").performClick()

            assertReplacedWith("£8.17", scenario)
        }
    }

    private fun top(text: String) = compose.onNodeWithText(text).fetchSemanticsNode().boundsInRoot.top

    private fun clipboard() = app.getSystemService(ClipboardManager::class.java)

    private fun assertReplacedWith(expected: String, scenario: ActivityScenario<Activity>) {
        assertEquals(Activity.RESULT_OK, scenario.result.resultCode)
        assertEquals(expected, scenario.result.resultData.getStringExtra(Intent.EXTRA_PROCESS_TEXT))
    }

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private fun intent(text: String) = Intent(Intent.ACTION_PROCESS_TEXT)
        .setClass(app, ConvertTextActivity::class.java)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_PROCESS_TEXT, text)

    private fun launch(text: String): ActivityScenario<Activity> =
        ActivityScenario.launchActivityForResult(intent(text))

    private fun awaitText(text: String) {
        compose.waitUntil(TIMEOUT_MS) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
    }

    private companion object {
        const val TIMEOUT_MS = 5_000L
        const val HIDE_NON_SYSTEM_OVERLAY_WINDOWS = 0x00080000
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val JPY: Currency = Currency.getInstance("JPY")
        val GBP: Currency = Currency.getInstance("GBP")
        val XAU: Currency = Currency.getInstance("XAU")
        val KRW: Currency = Currency.getInstance("KRW")
    }

}
