package cursola.rate.di

import android.util.Log
import cursola.rate.analytics.AnalyticService
import cursola.rate.analytics.PerformanceService
import spike.Include

@Include
internal fun analytics(): AnalyticService = object : AnalyticService {
    override fun log(event: String, params: Map<String, Any>) {
        Log.v("Analytics", "Event(name=$event,params=$params)")
    }
}

@Include
internal fun performance(): PerformanceService = object : PerformanceService {

    override suspend fun <T> network(
        url: String,
        method: String,
        body: suspend () -> T
    ): T {
        Log.v("Performance", "($method:$url) start")
        return body().also {
            Log.v("Performance", "($method:$url) stop")
        }
    }

    override suspend fun <T> trace(marker: String, body: suspend () -> T): T {
        Log.v("Performance", "($marker) start")
        return body().also {
            Log.v("Performance", "($marker) stop")
        }
    }

    override fun <T> traceInline(marker: String, body: () -> T): T {
        Log.v("Performance", "($marker) start")
        return body().also {
            Log.v("Performance", "($marker) stop")
        }
    }

}
