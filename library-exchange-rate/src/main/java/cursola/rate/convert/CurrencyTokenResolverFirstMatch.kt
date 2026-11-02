package cursola.rate.convert

import java.util.Locale

class CurrencyTokenResolverFirstMatch(
    private val resolvers: List<CurrencyTokenResolver>
) : CurrencyTokenResolver {

    override fun resolve(token: String, locale: Locale) =
        resolvers.firstNotNullOfOrNull { it.resolve(token, locale) }

}
