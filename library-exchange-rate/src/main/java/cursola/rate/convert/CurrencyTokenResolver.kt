package cursola.rate.convert

import java.util.Currency
import java.util.Locale

interface CurrencyTokenResolver {

    fun resolve(token: String, locale: Locale): Currency?

}
