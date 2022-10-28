package cursola.rate.network

import cursola.rate.ExchangeRate
import cursola.rate.analytics.PerformanceService

internal class ExchangeRateServicePerformance(
    private val origin: ExchangeRateService,
    private val performance: PerformanceService,
    private val url: String,
    private val method: String
) : ExchangeRateService {

    override suspend fun get(): List<ExchangeRate> {
        return performance.network(url, method) {
            origin.get()
        }
    }

}