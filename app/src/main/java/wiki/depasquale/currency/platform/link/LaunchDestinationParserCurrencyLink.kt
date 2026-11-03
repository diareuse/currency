package wiki.depasquale.currency.platform.link

import java.net.URI
import java.util.Currency

class LaunchDestinationParserCurrencyLink : LaunchDestinationParser {

    override fun parse(link: String?): LaunchDestination {
        val uri = link?.let { runCatching { URI(it) }.getOrNull() }
        if (uri?.scheme != CurrencyLink.SCHEME) return LaunchDestination.Start
        return when (uri.host) {
            CurrencyLink.HISTORY -> LaunchDestination.History
            CurrencyLink.CONVERT -> convert(uri.query.orEmpty())
            else -> LaunchDestination.Start
        }
    }

    private fun convert(query: String): LaunchDestination {
        val parameters = query.split('&').associate { it.substringBefore('=') to it.substringAfter('=', "") }
        val from = currency(parameters[CurrencyLink.FROM]) ?: return LaunchDestination.Start
        val to = currency(parameters[CurrencyLink.TO]) ?: return LaunchDestination.Start
        return LaunchDestination.Convert(CurrencyPair(from, to))
    }

    private fun currency(code: String?): Currency? =
        code?.let { runCatching { Currency.getInstance(it) }.getOrNull() }

}
