package wiki.depasquale.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import cursola.rate.view.ConvertTextViewModel
import cursola.rate.view.FavoritesViewModel
import cursola.rate.view.HistoricalViewModel
import cursola.rate.view.ListingViewModel
import org.junit.Test
import org.junit.runner.RunWith
import spike.lifecycle.viewmodel.ViewModelProviderFactory
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.di.viewModelEntryPointFactory
import wiki.depasquale.currency.platform.UpdateWorker
import wiki.depasquale.currency.screen.MainActivity
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
class AppGraphTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    fun app_graph_registers_every_screen_view_model() {
        assertEquals(
            setOf(
                ConvertTextViewModel::class,
                FavoritesViewModel::class,
                HistoricalViewModel::class,
                ListingViewModel::class
            ),
            app.appGraph.viewModels.keys
        )
    }

    @Test
    fun app_view_model_factory_creates_every_screen_view_model_for_an_activity() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val provider = activity.viewModelProvider()

                assertIs<FavoritesViewModel>(provider[FavoritesViewModel::class.java])
                assertIs<HistoricalViewModel>(provider[HistoricalViewModel::class.java])
                assertIs<ListingViewModel>(provider[ListingViewModel::class.java])
            }
        }
    }

    @Test
    fun view_models_are_shared_within_an_owner_and_distinct_across_owners() {
        val first = mutableListOf<ViewModel>()
        val second = mutableListOf<ViewModel>()
        ActivityScenario.launch(MainActivity::class.java).use { a ->
            ActivityScenario.launch(MainActivity::class.java).use { b ->
                a.onActivity { activity ->
                    repeat(2) { first += activity.viewModelProvider()[ListingViewModel::class.java] }
                }
                b.onActivity { activity -> second += activity.viewModelProvider()[ListingViewModel::class.java] }
            }
        }

        assertSame(first[0], first[1])
        assertNotSame(first[0], second[0])
    }

    @Test
    fun work_manager_configuration_creates_update_worker_bound_to_the_app() {
        val worker = TestListenableWorkerBuilder<UpdateWorker>(app)
            .setWorkerFactory(app.workManagerConfiguration.workerFactory)
            .build()

        assertIs<UpdateWorker>(worker)
        assertSame(app, worker.applicationContext)
    }

    private fun MainActivity.viewModelProvider(): ViewModelProvider {
        val factory = ViewModelProviderFactory.from(this, appGraph.viewModelEntryPointFactory)
        return ViewModelProvider(this, requireNotNull(factory))
    }

}
