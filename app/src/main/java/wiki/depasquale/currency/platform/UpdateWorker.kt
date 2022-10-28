package wiki.depasquale.currency.platform

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cursola.rate.ExchangeRateDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val exchangeRate: ExchangeRateDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return exchangeRate.runCatching { get() }.fold(
            onSuccess = { if (it.isEmpty()) Result.retry() else Result.success() },
            onFailure = { Result.retry() }
        )
    }

}