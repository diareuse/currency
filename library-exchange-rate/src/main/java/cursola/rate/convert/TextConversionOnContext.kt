package cursola.rate.convert

import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class TextConversionOnContext(
    private val origin: TextConversion,
    private val context: CoroutineContext
) : TextConversion {

    override suspend fun convert(text: CharSequence, locale: Locale) =
        withContext(context) { origin.convert(text, locale) }

}
