package cursola.rate.view

import cursola.rate.ExchangeRate
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class ConvertedCurrency(
    val currency: Currency,
    val value: Double,
    val isFavorite: Boolean
) {

    constructor(
        rate: ExchangeRate,
        isFavorite: Boolean
    ) : this(
        rate.currency,
        rate.rate,
        isFavorite
    )

    fun symbol(locale: Locale) =
        currency.getSymbol(locale).orEmpty()

    fun name(locale: Locale) =
        currency.getDisplayName(locale).orEmpty()

    fun toString(locale: Locale): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        format.currency = currency
        format.maximumFractionDigits = 2
        format.roundingMode = RoundingMode.HALF_UP
        return format.format(value)
    }

}