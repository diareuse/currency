package wiki.depasquale.currency.platform.widget

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.platform.UpdateWorker
import java.util.Currency
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class FavoritesWidgetRefreshTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Test
    fun successful_rate_update_refreshes_the_widget() {
        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(1, graph.widgetUpdates.updates)
    }

    @Test
    fun adding_and_removing_a_favorite_each_refresh_the_widget() {
        runBlocking { graph.favorites.add(EUR, 1) }
        val afterAdd = graph.widgetUpdates.updates

        runBlocking { graph.favorites.remove(EUR) }

        assertEquals(1, afterAdd)
        assertEquals(2, graph.widgetUpdates.updates)
    }

    @Test
    fun changing_the_latest_amount_refreshes_the_widget() {
        graph.latest().value = "250"

        assertEquals(1, graph.widgetUpdates.updates)
    }

    @Test
    fun changing_the_latest_currency_refreshes_the_widget() {
        graph.latest().currency = "USD"

        assertEquals(1, graph.widgetUpdates.updates)
    }

    @Test
    fun failing_widget_refresh_after_a_latest_amount_change_does_not_crash_the_app() {
        graph.widgetUpdates.failure = IllegalStateException("launcher went away")

        graph.latest().value = "250"

        assertEquals(1, graph.widgetUpdates.updates)
        assertEquals(emptyList(), graph.widgetUpdateFailures.errors.toList())
    }

    @Test
    fun rate_update_still_succeeds_when_refreshing_the_widget_fails() {
        graph.widgetUpdates.failure = IllegalStateException("launcher went away")

        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun favorite_is_still_added_when_refreshing_the_widget_fails() {
        graph.widgetUpdates.failure = IllegalStateException("launcher went away")

        runBlocking { graph.favorites.add(EUR, 1) }

        assertEquals(listOf(EUR), runBlocking { graph.favorites.list() })
    }

    @Test
    fun storing_the_same_latest_amount_again_does_not_refresh_the_widget() {
        graph.latest().value = "250"

        graph.latest().value = "250"

        assertEquals(1, graph.widgetUpdates.updates)
    }

    @Test
    fun storing_the_same_latest_currency_again_does_not_refresh_the_widget() {
        graph.latest().currency = "USD"

        graph.latest().currency = "USD"

        assertEquals(1, graph.widgetUpdates.updates)
    }

    private fun runUpdate() = runBlocking {
        TestListenableWorkerBuilder<UpdateWorker>(app)
            .setWorkerFactory(graph.workerFactory)
            .build()
            .doWork()
    }

    private companion object {
        val EUR: Currency = Currency.getInstance("EUR")
    }

}
