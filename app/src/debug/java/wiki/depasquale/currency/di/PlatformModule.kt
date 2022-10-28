package wiki.depasquale.currency.di

import android.util.Log
import cursola.rate.analytics.AnalyticService
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

}