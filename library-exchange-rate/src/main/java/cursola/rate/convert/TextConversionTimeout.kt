package cursola.rate.convert

import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import kotlin.time.Duration

class TextConversionTimeout(
    private val origin: TextConversion,
    private val timeout: Duration
) : TextConversion {

    override suspend fun convert(text: CharSequence, locale: Locale) =
        withTimeoutOrNull(timeout) { origin.convert(text, locale) } ?: TextConversionResult.Unavailable

}
