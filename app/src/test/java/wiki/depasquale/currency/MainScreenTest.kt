package wiki.depasquale.currency

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity
import wiki.depasquale.currency.screen.favorite.FavoriteItemTestTag
import java.util.Currency
import java.util.Date

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class MainScreenTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    @Test
    fun start_screen_shows_value_stored_through_the_app_graph() {
        ApplicationProvider.getApplicationContext<App>().appGraph.latest().value = "4242"

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.onNodeWithText("4242").assertExists()
        }
    }

    @Test
    fun start_screen_shows_each_favorite_as_the_shared_favorite_row_with_name_and_code() {
        val graph = ApplicationProvider.getApplicationContext<App>().appGraph as TestAppEntryPoint
        graph.exchangeRates.rates = listOf(ExchangeRate(EUR, 1.0, Date(0)), ExchangeRate(GBP, 0.8, Date(0)))
        runBlocking { graph.favorites.add(GBP, 1) }
        val row = hasTestTag(FavoriteItemTestTag) and hasText("British Pound") and hasText("GBP")

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.waitUntil(TIMEOUT_MS) { compose.onAllNodes(row).fetchSemanticsNodes().isNotEmpty() }
        }
    }

    private companion object {
        const val TIMEOUT_MS = 5_000L
        val EUR: Currency = Currency.getInstance("EUR")
        val GBP: Currency = Currency.getInstance("GBP")
    }

}
