package wiki.depasquale.currency.platform

import android.content.Context
import androidx.annotation.Keep
import androidx.startup.Initializer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import java.util.concurrent.TimeUnit

@Keep
class InitializerWork : Initializer<Unit> {

    override fun create(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<UpdateWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueueUniquePeriodicWork("rate-update", ExistingPeriodicWorkPolicy.KEEP, request)
    }

    override fun dependencies() =
        listOf(WorkManagerInitializer::class.java)

}