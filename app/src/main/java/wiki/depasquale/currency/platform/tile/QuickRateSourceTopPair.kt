package wiki.depasquale.currency.platform.tile

import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.Money
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import wiki.depasquale.currency.platform.link.CurrencyPair
import java.math.BigDecimal

class QuickRateSourceTopPair(
    private val topPair: TopFavoritePair,
    private val converter: CurrencyConverter
) : QuickRateSource {

    override suspend fun current(): QuickRate {
        val pair = topPair.current() ?: return QuickRate.NoPair
        return rateOf(pair)
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun rateOf(pair: CurrencyPair): QuickRate = try {
        QuickRate.Shown(pair, converter.convert(Money(BigDecimal.ONE, pair.from), pair.to).rate)
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        QuickRate.Unavailable(pair)
    }

}
