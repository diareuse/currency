package wiki.depasquale.currency.platform.widget

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.net.Uri
import androidx.glance.appwidget.testing.unit.GlanceAppWidgetUnitTest
import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.appwidget.testing.unit.hasStartActivityClickAction
import androidx.glance.testing.unit.hasAnyDescendant
import androidx.glance.testing.unit.hasTextEqualTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity
import java.io.IOException
import java.util.Currency
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
@Config(qualifiers = "en-rUS")
class FavoritesWidgetTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(0)),
            ExchangeRate(USD, 1.08423, Date(0)),
            ExchangeRate(GBP, 0.86372, Date(0)),
            ExchangeRate(JPY, 162.847, Date(0)),
        )
        graph.latest().currency = "EUR"
        graph.latest().value = "100"
    }

    @Test
    fun widget_shows_latest_amount_and_its_conversion_into_each_favorite_rounded_to_currency_digits() {
        favorites(USD, GBP, JPY)

        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("USD")).assertExists()
            onNode(hasTextEqualTo("$108.42")).assertExists()
            onNode(hasTextEqualTo("GBP")).assertExists()
            onNode(hasTextEqualTo("£86.37")).assertExists()
            onNode(hasTextEqualTo("JPY")).assertExists()
            onNode(hasTextEqualTo("¥16,285")).assertExists()
        }
    }

    @Test
    fun widget_without_favorites_shows_latest_amount_and_asks_to_add_favorites_in_the_app() {
        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("Add favorites in the app")).assertExists()
        }
    }

    @Test
    fun widget_whose_only_favorite_is_the_latest_currency_asks_to_add_favorites_in_the_app() {
        favorites(EUR)

        widget {
            onNode(hasTextEqualTo("Add favorites in the app")).assertExists()
            onNode(hasTextEqualTo("Rates unavailable")).assertDoesNotExist()
        }
    }

    @Test
    fun widget_whose_favorites_have_no_rate_reports_rates_unavailable_under_latest_amount() {
        favorites(CHF)

        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("Rates unavailable")).assertExists()
        }
    }

    @Test
    fun widget_reports_rates_unavailable_under_latest_amount_when_rate_lookup_fails() {
        favorites(USD, GBP)
        graph.exchangeRates.failure = IOException("offline")

        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("Rates unavailable")).assertExists()
        }
    }

    @Test
    fun widget_reports_rates_unavailable_under_latest_amount_when_reading_favorites_fails() {
        favorites(USD, GBP)
        graph.favoriteReadFailure.value = SQLiteException("database is locked")

        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("Rates unavailable")).assertExists()
        }
    }

    @Test
    fun widget_gives_up_on_a_hung_rate_lookup_and_reports_rates_unavailable_under_latest_amount() {
        favorites(USD, GBP)
        graph.exchangeRates.hanging = true
        graph.favoritesWidgetDeadline.value = 100.milliseconds

        widget {
            onNode(hasTextEqualTo("€100.00")).assertExists()
            onNode(hasTextEqualTo("Rates unavailable")).assertExists()
        }
    }

    @Test
    fun widget_reports_rates_unavailable_when_the_stored_currency_is_not_a_currency() {
        favorites(USD, GBP)
        graph.latest().currency = "not a currency"

        widget {
            onNode(hasTextEqualTo("Rates unavailable")).assertExists()
            onNode(hasTextEqualTo("USD")).assertDoesNotExist()
        }
    }

    @Test
    fun widget_converts_an_unparseable_stored_amount_as_zero_like_the_converter_screen() {
        favorites(USD)
        graph.latest().value = "-"

        widget {
            onNode(hasTextEqualTo("€0.00")).assertExists()
            onNode(hasTextEqualTo("$0.00")).assertExists()
        }
    }

    @Test
    fun tapping_widget_opens_the_app_without_a_preselected_pair() {
        favorites(USD, GBP)

        widget {
            onNode(hasStartActivityClickAction(Intent(app, MainActivity::class.java)))
                .assertExists()
                .assert(hasAnyDescendant(hasTextEqualTo("€100.00")))
        }
    }

    @Test
    fun tapping_widget_without_favorites_opens_the_app_to_add_them() {
        widget {
            onNode(hasStartActivityClickAction(Intent(app, MainActivity::class.java)))
                .assertExists()
                .assert(hasAnyDescendant(hasTextEqualTo("Add favorites in the app")))
        }
    }

    @Test
    fun tapping_a_conversion_row_opens_the_converter_on_that_pair() {
        favorites(USD, GBP)

        widget {
            onNode(hasStartActivityClickAction(convertLink("EUR", "USD")))
                .assertExists()
                .assert(hasAnyDescendant(hasTextEqualTo("$108.42")))
            onNode(hasStartActivityClickAction(convertLink("EUR", "GBP")))
                .assertExists()
                .assert(hasAnyDescendant(hasTextEqualTo("£86.37")))
        }
    }

    @Test
    fun widget_shows_favorites_that_have_a_rate_and_leaves_out_those_without_one() {
        favorites(USD, CHF)

        widget {
            onNode(hasTextEqualTo("$108.42")).assertExists()
            onNode(hasTextEqualTo("CHF")).assertDoesNotExist()
            onNode(hasTextEqualTo("Rates unavailable")).assertDoesNotExist()
        }
    }

    @Test
    fun widget_leaves_the_latest_currency_out_of_its_conversion_rows() {
        favorites(EUR, USD)

        widget {
            onNode(hasTextEqualTo("$108.42")).assertExists()
            onNode(hasTextEqualTo("EUR")).assertDoesNotExist()
        }
    }

    private fun convertLink(from: String, to: String) =
        Intent(Intent.ACTION_VIEW, Uri.parse("currency://convert?from=$from&to=$to")).setClass(app, MainActivity::class.java)

    private fun widget(assertions: GlanceAppWidgetUnitTest.() -> Unit) {
        val snapshot = runBlocking { withTimeout(LOAD_LIMIT) { graph.favoritesSnapshot.current() } }
        runGlanceAppWidgetUnitTest {
            setContext(app)
            provideComposable { FavoritesWidgetContent(snapshot, graph.moneyFormat, graph.converterLaunchIntent) }
            assertions()
        }
    }

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private companion object {
        val LOAD_LIMIT = 5.seconds
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val GBP: Currency = Currency.getInstance("GBP")
        val JPY: Currency = Currency.getInstance("JPY")
        val CHF: Currency = Currency.getInstance("CHF")
    }

}
