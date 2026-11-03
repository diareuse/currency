package wiki.depasquale.currency.platform.tile

import wiki.depasquale.currency.platform.link.CurrencyPair

sealed interface QuickRate {

    data object NoPair : QuickRate

    data class Unavailable(val pair: CurrencyPair?) : QuickRate

    data class Shown(val pair: CurrencyPair, val rate: Double) : QuickRate

}

val QuickRate.pair: CurrencyPair?
    get() = when (this) {
        QuickRate.NoPair -> null
        is QuickRate.Unavailable -> pair
        is QuickRate.Shown -> pair
    }
