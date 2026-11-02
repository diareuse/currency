package wiki.depasquale.currency

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ProviderInfo
import androidx.startup.InitializationProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.NetworkType
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import wiki.depasquale.currency.platform.InitializerWork
import wiki.depasquale.currency.platform.UpdateWorker
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
class ColdStartTest {

    private val app = attachedButNotCreatedApp()

    @After
    fun releaseWorkManager() {
        WorkManager.getInstance(app).cancelUniqueWork("rate-update").result.get(10, TimeUnit.SECONDS)
        WorkManagerTestInitHelper.closeWorkDatabase()
    }

    @Test
    fun startup_before_application_onCreate_schedules_one_daily_connected_update_runnable_by_configured_factory() {
        assertFalse(WorkManager.isInitialized(), "WorkManager leaked from another test")

        InitializationProvider().attachInfo(app, ProviderInfo().apply {
            authority = "${app.packageName}.androidx-startup"
        })
        val workManager = WorkManager.getInstance(app)
        val first = workManager.uniqueRateUpdate()
        InitializerWork().create(app)
        val second = workManager.uniqueRateUpdate()
        val worker = TestListenableWorkerBuilder<UpdateWorker>(app)
            .setWorkerFactory(workManager.configuration.workerFactory)
            .build()

        assertEquals(1, first.size)
        assertEquals(NetworkType.CONNECTED, first.single().constraints.requiredNetworkType)
        assertEquals(TimeUnit.DAYS.toMillis(1), first.single().periodicityInfo?.repeatIntervalMillis)
        assertEquals(first.map { it.id }, second.map { it.id })
        assertIs<UpdateWorker>(worker)
        assertSame(app, worker.applicationContext)
    }

    private fun WorkManager.uniqueRateUpdate() =
        getWorkInfosForUniqueWork("rate-update").get(10, TimeUnit.SECONDS)

    private fun attachedButNotCreatedApp(): App {
        val base = ApplicationProvider.getApplicationContext<Context>()
        val app = TestApp()
        shadowOf(app).callAttach(object : ContextWrapper(base) {
            override fun getApplicationContext(): Context = app
        })
        return app
    }

}
