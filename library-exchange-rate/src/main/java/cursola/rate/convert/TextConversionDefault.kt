package cursola.rate.convert

import cursola.rate.FavoriteDataSource
import java.util.Locale

class TextConversionDefault(
    private val parser: AmountParser,
    private val favorites: FavoriteDataSource,
    private val converter: CurrencyConverter
) : TextConversion {

    override suspend fun convert(text: CharSequence, locale: Locale): TextConversionResult {
        val source = parser.parse(text, locale) ?: return TextConversionResult.Unparseable
        if (favorites.list().none { it != source.currency }) return TextConversionResult.NoFavorites
        val conversions = converter.convertToFavorites(source)
        if (conversions.isEmpty()) return TextConversionResult.Unavailable
        return TextConversionResult.Converted(conversions)
    }

}
