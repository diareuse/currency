package cursola.rate.di

import cursola.rate.analytics.AnalyticService
import cursola.rate.analytics.PerformanceService
import spike.Include

@Include
internal fun analytics(): AnalyticService = object : AnalyticService {
    override fun log(event: String, params: Map<String, Any>) {
        //Firebase.analytics.logEvent(event, bundleOf(pairs = params.toList().toTypedArray()))
    }
}

@Include
internal fun performance(): PerformanceService = object : PerformanceService {

    //private val performance = Firebase.performance

    override suspend fun <T> network(
        url: String,
        method: String,
        body: suspend () -> T
    ): T {
        /*val metric = performance.newHttpMetric(url, method)
        metric.start()
        try {*/
        return body()
        /*} finally {
            metric.stop()
        }*/
    }

    override suspend fun <T> trace(marker: String, body: suspend () -> T): T {
        //return performance.newTrace(marker).trace {
        return body()
        //}
    }

    override fun <T> traceInline(marker: String, body: () -> T): T {
        //return performance.newTrace(marker).trace {
        return body()
        //}
    }

}
