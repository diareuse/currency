package wiki.depasquale.currency.platform.tile

import java.util.Locale

fun interface RateFormat {

    fun format(rate: Double, locale: Locale): String

}
