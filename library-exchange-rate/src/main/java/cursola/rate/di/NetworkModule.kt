package cursola.rate.di

import cursola.rate.analytics.PerformanceService
import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServiceImpl
import cursola.rate.network.ExchangeRateServicePerformance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@InstallIn(ActivityRetainedComponent::class)
@Module
internal class NetworkModule {

    @Provides
    fun service(
        client: HttpClient,
        performance: PerformanceService
    ): ExchangeRateService {
        val url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"
        return serviceWithUrl(client, performance, url)
    }

    @ScopeOf90Days
    @Provides
    fun service90Days(
        client: HttpClient,
        performance: PerformanceService
    ): ExchangeRateService {
        val url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml"
        return serviceWithUrl(client, performance, url)
    }

    @ScopeSinceInception
    @Provides
    fun serviceInception(
        client: HttpClient,
        performance: PerformanceService
    ): ExchangeRateService {
        val url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml"
        return serviceWithUrl(client, performance, url)
    }

    @Provides
    fun client() = HttpClient(CIO) {}

    // ---

    private fun serviceWithUrl(
        client: HttpClient,
        performance: PerformanceService,
        url: String
    ): ExchangeRateService {
        val method = "GET"
        var service: ExchangeRateService
        service = ExchangeRateServiceImpl(client, url)
        service = ExchangeRateServicePerformance(service, performance, url, method)
        return service
    }

}