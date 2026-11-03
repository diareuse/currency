package cursola.rate.view

sealed interface ConvertTextState {

    data object Loading : ConvertTextState

    data object Unparseable : ConvertTextState

    data object NoFavorites : ConvertTextState

    data object Unavailable : ConvertTextState

    data class Converted(val rows: List<ConvertTextRow>) : ConvertTextState

}
