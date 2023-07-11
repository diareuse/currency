package wiki.depasquale.currency.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.list
import cursola.rate.ConversionRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.LatestValueDataSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SliceProvider : SliceProvider() {

    private lateinit var scope: CoroutineScope
    private lateinit var rates: CurrencyRates

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SliceEntryPoint {
        fun conversion(): ConversionRateDataSource
        fun favorite(): FavoriteDataSource
        fun latest(): LatestValueDataSource

        companion object {

            private var entry: SliceEntryPoint? = null

            fun get(): SliceEntryPoint {
                return requireNotNull(entry)
            }

            fun create(context: Context): SliceEntryPoint {
                val appContext = checkNotNull(context.applicationContext)
                val hiltEntryPoint = EntryPointAccessors.fromApplication(
                    context = appContext,
                    entryPoint = SliceEntryPoint::class.java
                )
                entry = hiltEntryPoint
                return hiltEntryPoint
            }
        }
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        val context = checkNotNull(context)
        check(sliceUri.authority == context.packageName)
        if (!::rates.isInitialized)
            rates = when (sliceUri.pathSegments) {
                CurrencyRates.segments -> CurrencyRates(context, SliceEntryPoint.get())
                else -> return null
            }
        scope.launch {
            if (rates.load())
                context.contentResolver.notifyChange(sliceUri, null)
        }
        return list(context, sliceUri, 1000) {
            rates.apply {
                create()
            }
        }
    }

    override fun onCreateSliceProvider(): Boolean {
        SliceEntryPoint.create(context!!)
        scope = CoroutineScope(SupervisorJob())
        return true
    }

}

