package cursola.rate.convert

sealed interface TextConversionResult {

    data object Unparseable : TextConversionResult

    data object NoFavorites : TextConversionResult

    data object Unavailable : TextConversionResult

    data class Converted(val conversions: List<Conversion>) : TextConversionResult

}
