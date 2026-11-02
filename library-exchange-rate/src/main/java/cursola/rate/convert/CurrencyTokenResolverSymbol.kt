package cursola.rate.convert

import java.util.Currency
import java.util.Locale

class CurrencyTokenResolverSymbol : CurrencyTokenResolver {

    override fun resolve(token: String, locale: Locale): Currency? {
        val symbol = token.uppercase(Locale.ROOT)
        val code = when (symbol) {
            "$" -> localDollar(locale)
            "¥", "￥" -> if (locale.country == "CN") "CNY" else "JPY"
            else -> fixed[symbol]
        }
        return code?.let(Currency::getInstance)
    }

    private fun localDollar(locale: Locale): String {
        val local = runCatching { Currency.getInstance(locale) }.getOrNull()
        val usesDollar = local?.getSymbol(locale)?.contains('$') == true
        return if (usesDollar) local.currencyCode else "USD"
    }

    private companion object {
        val fixed = mapOf(
            "US$" to "USD",
            "A$" to "AUD",
            "AU$" to "AUD",
            "C$" to "CAD",
            "CA$" to "CAD",
            "NZ$" to "NZD",
            "HK$" to "HKD",
            "S$" to "SGD",
            "R$" to "BRL",
            "MX$" to "MXN",
            "€" to "EUR",
            "£" to "GBP",
            "₹" to "INR",
            "₽" to "RUB",
            "₩" to "KRW",
            "₺" to "TRY",
            "₴" to "UAH",
            "₪" to "ILS",
            "฿" to "THB",
            "₫" to "VND",
            "₱" to "PHP",
            "₦" to "NGN",
            "KČ" to "CZK",
            "ZŁ" to "PLN",
        )
    }

}
