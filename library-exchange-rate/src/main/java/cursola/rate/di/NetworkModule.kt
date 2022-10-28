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
        val method = "GET"
        var service: ExchangeRateService
        service = ExchangeRateServiceImpl(client, url)
        service = ExchangeRateServicePerformance(service, performance, url, method)
        return service
    }

    @Provides
    fun client() = HttpClient(CIO) {}

}