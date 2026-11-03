package wiki.depasquale.currency.platform.widget

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.TextView
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.appwidget.runComposition
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGlanceApi::class)
@RunWith(AndroidJUnit4::class)
@Config(qualifiers = "en-rUS")
class FavoritesWidgetSessionTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(0)),
            ExchangeRate(USD, 1.08423, Date(0)),
        )
        graph.latest().currency = "EUR"
        graph.latest().value = "100"
        runBlocking { graph.favorites.add(USD, 1) }
    }

    @Test
    fun placed_widget_shows_the_latest_amount(): Unit = runBlocking {
        val shown = withTimeout(LIMIT) { FavoritesWidget().runComposition(app).first() }

        assertTrue("€100.00" in shown.texts(), "shown: ${shown.texts()}")
    }

    @Test
    fun running_widget_shows_the_new_latest_amount_after_it_changes(): Unit = runBlocking {
        val frames = FavoritesWidget().runComposition(app).map { it.texts() }
            .stateIn(this + Dispatchers.Default, SharingStarted.Eagerly, emptyList())
        try {
            withTimeout(LIMIT) { frames.first { "€100.00" in it } }

            graph.latest().value = "250"

            withTimeout(LIMIT) { frames.first { "€250.00" in it } }
        } finally {
            coroutineContext.cancelChildren()
        }
    }

    @Test
    fun running_widget_shows_new_conversions_after_favorites_change(): Unit = runBlocking {
        graph.latest().currency = "USD"
        val frames = FavoritesWidget().runComposition(app).map { it.texts() }
            .stateIn(this + Dispatchers.Default, SharingStarted.Eagerly, emptyList())
        try {
            withTimeout(LIMIT) { frames.first { "Add favorites in the app" in it } }

            graph.favorites.add(EUR, 2)

            val converted = withTimeout(LIMIT) { frames.first { "Add favorites in the app" !in it && it.isNotEmpty() } }
            assertEquals(listOf("$100.00"), converted)
        } finally {
            coroutineContext.cancelChildren()
        }
    }

    private fun RemoteViews.texts(): List<String> = apply(app, FrameLayout(app)).texts()

    private fun View.texts(): List<String> = when (this) {
        is TextView -> listOf(text.toString())
        is ViewGroup -> (0 until childCount).flatMap { getChildAt(it).texts() }
        else -> emptyList()
    }

    private companion object {
        val LIMIT = 5.seconds
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
    }

}
