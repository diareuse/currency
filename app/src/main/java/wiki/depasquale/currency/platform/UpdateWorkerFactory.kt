package wiki.depasquale.currency.platform

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import spike.Include

@Include
class UpdateWorkerFactory(
    private val updateWorker: (WorkerParameters) -> UpdateWorker
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        UpdateWorker::class.java.name -> updateWorker(workerParameters)
        else -> null
    }

}

@Include
val UpdateWorkerFactory.asWorkerFactory: WorkerFactory
    get() = this
