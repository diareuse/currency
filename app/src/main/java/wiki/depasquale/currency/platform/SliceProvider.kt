package wiki.depasquale.currency.platform

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import wiki.depasquale.currency.di.appGraph

class SliceProvider : SliceProvider() {

    private lateinit var scope: CoroutineScope
    private lateinit var rates: CurrencyRates

    interface SliceEntryPoint {
        fun conversion(): ConversionRateDataSource
        fun favorite(): FavoriteDataSource
        fun latest(): LatestValueDataSource
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        val context = checkNotNull(context)
        check(sliceUri.authority == context.packageName)
        if (!::rates.isInitialized)
            rates = when (sliceUri.pathSegments) {
                CurrencyRates.segments -> CurrencyRates(context, context.appGraph)
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
        scope = CoroutineScope(SupervisorJob())
        return true
    }

}

