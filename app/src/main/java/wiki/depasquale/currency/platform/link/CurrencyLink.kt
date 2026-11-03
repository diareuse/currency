package wiki.depasquale.currency.platform.link

object CurrencyLink {

    const val SCHEME = "currency"
    const val CONVERT = "convert"
    const val HISTORY = "history"
    const val FROM = "from"
    const val TO = "to"

}

fun CurrencyPair.toConvertLink(): String = with(CurrencyLink) {
    "$SCHEME://$CONVERT?$FROM=${from.currencyCode}&$TO=${to.currencyCode}"
}
