package wiki.depasquale.currency.platform.tile

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.platform.UpdateWorker
import java.util.Currency
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(shadows = [RejectingStatusBarManager::class])
class QuickRateTileNeverAddedTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Test
    fun tile_that_was_never_added_neither_fails_the_rate_update_nor_breaks_favorite_edits() {
        val result = runBlocking {
            TestListenableWorkerBuilder<UpdateWorker>(app)
                .setWorkerFactory(graph.workerFactory)
                .build()
                .doWork()
        }
        runBlocking { graph.favorites.add(EUR, 1) }

        assertEquals(ListenableWorker.Result.success(), result)
        assertEquals(listOf(EUR), runBlocking { graph.favorites.list() })
    }

    private companion object {
        val EUR: Currency = Currency.getInstance("EUR")
    }

}
