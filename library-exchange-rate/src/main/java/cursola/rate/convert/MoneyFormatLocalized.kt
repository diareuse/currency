package cursola.rate.convert

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class MoneyFormatLocalized(
    private val fractionDigits: CurrencyFractionDigits
) : MoneyFormat {

    override fun format(money: Money, locale: Locale): String {
        val base = baseDigits(money.currency)
        val format = NumberFormat.getCurrencyInstance(locale)
        format.currency = money.currency
        format.roundingMode = RoundingMode.HALF_UP
        format.minimumFractionDigits = base
        format.maximumFractionDigits = maxOf(base, significantScale(money.amount, base))
        return format.format(money.amount)
    }

    private fun baseDigits(currency: Currency): Int {
        val digits = fractionDigits.of(currency)
        return if (digits < 0) UNSPECIFIED_DIGITS else digits
    }

    private fun significantScale(amount: BigDecimal, base: Int): Int {
        if (base == 0 || amount.signum() == 0) return 0
        if (amount.abs() >= BigDecimal.ONE.movePointLeft(base)) return base
        return amount.round(significantDigits).stripTrailingZeros().scale()
    }

    private companion object {
        const val UNSPECIFIED_DIGITS = 2
        val significantDigits = MathContext(4, RoundingMode.HALF_UP)
    }

}
