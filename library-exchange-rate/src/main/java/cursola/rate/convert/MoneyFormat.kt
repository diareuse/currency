package cursola.rate.convert

import java.util.Locale

fun interface MoneyFormat {

    fun format(money: Money, locale: Locale): String

}
