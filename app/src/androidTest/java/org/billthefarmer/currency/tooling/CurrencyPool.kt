package org.billthefarmer.currency.tooling

import java.util.*

object CurrencyPool {

    val currencies = listOf(
        Currency.getInstance("USD"),
        Currency.getInstance("EUR"),
        Currency.getInstance("PHP"),
        Currency.getInstance("CZK"),
    )

    fun random() = currencies.random()

}