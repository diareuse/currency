package wiki.depasquale.currency.platform

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cursola.rate.ExchangeRateDataSource
import spike.Include

@Include
class UpdateWorker(
    context: Context,
    params: WorkerParameters,
    private val exchangeRate: ExchangeRateDataSource,
    private val ratesUpdated: RatesUpdatedListener
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val rates = exchangeRate.runCatching { get() }.getOrElse { return Result.retry() }
        if (rates.isEmpty()) return Result.retry()
        ratesUpdated.onRatesUpdated()
        return Result.success()
    }

}
