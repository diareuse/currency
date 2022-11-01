package wiki.depasquale.currency.di

import cursola.rate.analytics.AnalyticService
import cursola.rate.analytics.PerformanceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class PlatformModule {

    @Provides
    fun analytics() = object : AnalyticService {
        override fun log(event: String, params: Map<String, Any>) {
            /* no-op */
        }
    }

    @Provides
    fun performance() = object : PerformanceService {

        override suspend fun <T> network(
            url: String,
            method: String,
            body: suspend () -> T
        ): T {
            return body()
        }

        override suspend fun <T> trace(marker: String, body: suspend () -> T): T {
            return body()
        }

        override fun <T> traceInline(marker: String, body: () -> T): T {
            return body()
        }

    }

}