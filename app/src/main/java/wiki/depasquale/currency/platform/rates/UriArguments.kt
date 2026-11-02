package wiki.depasquale.currency.platform.rates

import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

internal fun currencyOf(code: String?, name: String): Currency {
    requireNotNull(code) { "Missing $name" }
    return try {
        Currency.getInstance(code.uppercase(Locale.ROOT))
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException("Unknown currency $code for $name", e)
    }
}

private val plainDecimal = Regex("-?\\d{1,18}(\\.\\d{1,18})?")

internal fun amountOf(value: String?, name: String): BigDecimal {
    requireNotNull(value) { "Missing $name" }
    require(value.matches(plainDecimal)) { "Malformed $name $value" }
    return BigDecimal(value)
}
