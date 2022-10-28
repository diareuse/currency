package cursola.rate.analytics

interface PerformanceService {

    suspend fun <T> network(
        url: String,
        method: String,
        body: suspend () -> T
    ): T

    suspend fun <T> trace(
        marker: String,
        body: suspend () -> T
    ): T

    fun <T> traceInline(
        marker: String,
        body: () -> T
    ): T

}