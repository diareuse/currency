package wiki.depasquale.currency.presentation.view.common

import wiki.depasquale.currency.domain.rate.RateCalculator

object ExchangeRateItemDefaults {

    val Calculator: RateCalculator = RateCalculator { it }

}