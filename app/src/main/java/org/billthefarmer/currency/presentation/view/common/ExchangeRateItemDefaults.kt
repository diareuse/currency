package org.billthefarmer.currency.presentation.view.common

import org.billthefarmer.currency.domain.rate.RateCalculator

object ExchangeRateItemDefaults {

    val Calculator: RateCalculator = RateCalculator { it }

}