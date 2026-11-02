package cursola.rate.convert

import java.util.Currency
import java.util.Locale

class CurrencyTokenResolverIso : CurrencyTokenResolver {

    override fun resolve(token: String, locale: Locale): Currency? {
        val code = token.uppercase(Locale.ROOT)
        if (!code.matches(isoCode)) return null
        return try {
            Currency.getInstance(code)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    private companion object {
        val isoCode = Regex("[A-Z]{3}")
    }

}
