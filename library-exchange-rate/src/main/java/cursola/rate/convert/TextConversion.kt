package cursola.rate.convert

import java.util.Locale

interface TextConversion {

    suspend fun convert(text: CharSequence, locale: Locale): TextConversionResult

}
