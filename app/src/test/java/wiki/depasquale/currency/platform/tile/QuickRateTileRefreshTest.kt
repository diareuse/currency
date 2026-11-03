package wiki.depasquale.currency.platform.tile

import android.app.StatusBarManager
import android.content.ComponentName
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.platform.UpdateWorker
import java.util.Currency
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(shadows = [RecordingStatusBarManager::class])
class QuickRateTileRefreshTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint
    private val tile get() = ComponentName(app, QuickRateTileService::class.java)

    @Test
    fun successful_rate_update_asks_the_tile_to_refresh() {
        val result = runUpdate()

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(listOf(tile), statusBar().listeningRequests)
    }

    @Test
    fun adding_and_removing_a_favorite_each_ask_the_tile_to_refresh() {
        runBlocking { graph.favorites.add(EUR, 1) }
        val afterAdd = statusBar().listeningRequests.toList()

        runBlocking { graph.favorites.remove(EUR) }

        assertEquals(listOf(tile), afterAdd)
        assertEquals(listOf(tile, tile), statusBar().listeningRequests)
    }

    @Test
    fun newly_added_tile_asks_to_be_refreshed() {
        val service = Robolectric.buildService(QuickRateTileService::class.java).create().get()

        service.onTileAdded()

        assertEquals(listOf(tile), statusBar().listeningRequests)
    }

    private fun runUpdate() = runBlocking {
        TestListenableWorkerBuilder<UpdateWorker>(app)
            .setWorkerFactory(graph.workerFactory)
            .build()
            .doWork()
    }

    private fun statusBar() =
        Shadow.extract<RecordingStatusBarManager>(app.getSystemService(StatusBarManager::class.java))

    private companion object {
        val EUR: Currency = Currency.getInstance("EUR")
    }

}
