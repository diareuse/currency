package org.billthefarmer.currency.domain.model

import java.util.*

data class ExchangeRate(
    val currency: Currency,
    val rate: Double,
    val time: Date
) {

    companion object {

        val Default = ExchangeRate(
            currency = Currency.getInstance("EUR"),
            rate = 1.0,
            time = Date(0)
        )

    }

}