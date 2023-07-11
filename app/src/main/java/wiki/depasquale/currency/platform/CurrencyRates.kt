package wiki.depasquale.currency.platform

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.ListBuilderDsl
import androidx.slice.builders.cell
import androidx.slice.builders.gridRow
import androidx.slice.builders.header
import androidx.slice.builders.tapSliceAction
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import cursola.rate.view.ConvertedCurrency
import wiki.depasquale.currency.R
import wiki.depasquale.currency.platform.CurrencyRates.FlaggedCurrency.Companion.withFlag
import wiki.depasquale.currency.screen.MainActivity
import java.util.Currency
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CurrencyRates(
    private val context: Context,
    private val entry: SliceProvider.SliceEntryPoint
) {

    private var converted: FlaggedCurrency? = null
    private var currencies: List<FlaggedCurrency> = emptyList()

    private val isLoading get() = converted == null
    private val locale = Locale.getDefault()

    suspend fun load(): Boolean {
        if (!isLoading) return false
        val latest = entry.latest()
        val selected = Currency.getInstance(latest.currency)
        val value = latest.value.toDoubleOrNull() ?: 0.0
        converted = ConvertedCurrency(selected, value, true).withFlag(context)
        val conversion = entry.conversion()
        currencies = entry.favorite().list().filter { it != converted?.currency?.currency }.map {
            ConvertedCurrency(it, value * conversion.get(selected, it), true).withFlag(context)
        }
        return true
    }

    fun ListBuilderDsl.create() {
        header {
            setTitle(context.getString(R.string.app_name))
            setSubtitle(converted?.currency?.toString(locale).orEmpty(), isLoading)
        }
        gridRow {
            primaryAction = tapSliceAction(
                pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                ),
                icon = IconCompat.createWithResource(context, R.drawable.ic_history),
                imageMode = ListBuilder.ICON_IMAGE,
                title = ""
            )
            for (currency in currencies) cell {
                val icon = IconCompat.createWithBitmap(currency.bitmap)
                addImage(icon, ListBuilder.RAW_IMAGE_SMALL)
                addTitleText(currency.currency.name(locale))
                addText(currency.currency.toString(locale))
            }
        }
    }

    class FlaggedCurrency(
        val currency: ConvertedCurrency,
        val bitmap: Bitmap
    ) {

        companion object {

            suspend fun ConvertedCurrency.withFlag(context: Context): FlaggedCurrency {
                val resId = currency.asId(context.resources, context.packageName)
                val country = context.getString(resId).lowercase()
                val bytes = suspendCoroutine { cont ->
                    val request = ImageRequest.Builder(context)
                        .data("https://flagicons.lipis.dev/flags/4x3/${country}.svg")
                        .decoderFactory(SvgDecoder.Factory())
                        .transformations(RoundedCornersTransformation(20f))
                        .target { cont.resume(it.toBitmap()) }
                        .build()
                    ImageLoader(context).enqueue(request)
                }
                return FlaggedCurrency(this, bytes)
            }

            @SuppressLint("DiscouragedApi")
            private fun Currency.asId(res: Resources, packageName: String): Int {
                val id = res.getIdentifier(currencyCode.uppercase(), "string", packageName)
                if (id == 0) {
                    return cursola.view.R.string.EUR
                }
                return id
            }
        }
    }

    companion object {
        val segments = listOf("conversions")
    }

}