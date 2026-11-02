package wiki.depasquale.currency

import android.app.Application
import androidx.work.Configuration
import com.google.android.material.color.DynamicColors
import wiki.depasquale.currency.di.AppEntryPoint
import wiki.depasquale.currency.di.invoke

open class App : Application(), Configuration.Provider {

    val graph: AppEntryPoint by lazy { createGraph() }

    protected open fun createGraph(): AppEntryPoint = AppEntryPoint(this)

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(graph.workerFactory)
            .build()

}
