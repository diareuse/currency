package wiki.depasquale.currency.di

import androidx.core.os.bundleOf
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
            Firebase.analytics.logEvent(event, bundleOf(pairs = params.toList().toTypedArray()))
        }
    }

}