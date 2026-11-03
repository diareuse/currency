package cursola.rate.convert

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.util.Locale

class TextConversionErrorBoundary(
    private val origin: TextConversion
) : TextConversion {

    override suspend fun convert(text: CharSequence, locale: Locale): TextConversionResult = try {
        origin.convert(text, locale)
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        TextConversionResult.Unavailable
    }

}
