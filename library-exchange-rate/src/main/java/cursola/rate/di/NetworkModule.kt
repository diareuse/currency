package cursola.rate.di

import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServiceImpl
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
        client: HttpClient
    ): ExchangeRateService {
        return ExchangeRateServiceImpl(
            client,
            "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"
        )
    }

    @Provides
    fun client() = HttpClient(CIO) {}

}