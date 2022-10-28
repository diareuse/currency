package wiki.depasquale.currency.di

import android.util.Log
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
            Log.v("Analytics", "Event(name=$event,params=$params)")
        }
    }

    @Provides
    fun performance() = object : PerformanceService {

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

}