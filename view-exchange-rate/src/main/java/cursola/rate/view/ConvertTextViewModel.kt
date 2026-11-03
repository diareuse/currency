package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.convert.Conversion
import cursola.rate.convert.MoneyFormat
import cursola.rate.convert.TextConversion
import cursola.rate.convert.TextConversionResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import spike.lifecycle.viewmodel.SpikeViewModel
import java.util.Locale

@SpikeViewModel
class ConvertTextViewModel internal constructor(
    private val conversion: TextConversion,
    private val format: MoneyFormat
) : ViewModel() {

    private val mutableState = MutableStateFlow<ConvertTextState>(ConvertTextState.Loading)
    val state: StateFlow<ConvertTextState> = mutableState.asStateFlow()

    private var request: ConvertTextRequest? = null
    private var job: Job? = null

    fun convert(text: CharSequence, locale: Locale) {
        val next = ConvertTextRequest(text.toString(), locale)
        if (next == request) return
        request = next
        job?.cancel()
        mutableState.value = ConvertTextState.Loading
        job = viewModelScope.launch { mutableState.value = render(conversion.convert(text, locale), locale) }
    }

    private fun render(result: TextConversionResult, locale: Locale) = when (result) {
        TextConversionResult.Unparseable -> ConvertTextState.Unparseable
        TextConversionResult.NoFavorites -> ConvertTextState.NoFavorites
        TextConversionResult.Unavailable -> ConvertTextState.Unavailable
        is TextConversionResult.Converted -> ConvertTextState.Converted(result.conversions.map { row(it, locale) })
    }

    private data class ConvertTextRequest(val text: String, val locale: Locale)

    private fun row(conversion: Conversion, locale: Locale) = ConvertTextRow(
        name = conversion.result.currency.getDisplayName(locale),
        value = format.format(conversion.result, locale)
    )

}
