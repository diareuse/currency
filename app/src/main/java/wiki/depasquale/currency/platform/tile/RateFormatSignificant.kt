package wiki.depasquale.currency.platform.tile

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class RateFormatSignificant(
    private val digits: Int
) : RateFormat {

    override fun format(rate: Double, locale: Locale): String {
        val rounded = BigDecimal.valueOf(rate).round(MathContext(digits, RoundingMode.HALF_UP))
        val integerDigits = rounded.precision() - rounded.scale()
        val format = NumberFormat.getNumberInstance(locale)
        format.minimumFractionDigits = maxOf(0, digits - integerDigits)
        format.maximumFractionDigits = format.minimumFractionDigits
        return format.format(rounded)
    }

}
