package cursola.rate.convert

import java.util.Currency

fun interface CurrencyFractionDigits {

    fun of(currency: Currency): Int

}
