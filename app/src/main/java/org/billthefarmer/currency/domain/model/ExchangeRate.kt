package org.billthefarmer.currency.domain.model

import java.util.*

data class ExchangeRate(
    val currency: Currency,
    val rate: Double,
    val time: Date
)