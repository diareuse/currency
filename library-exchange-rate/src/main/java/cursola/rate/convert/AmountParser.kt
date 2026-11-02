package cursola.rate.convert

import java.util.Locale

interface AmountParser {

    fun parse(text: CharSequence, locale: Locale): Money?

}
