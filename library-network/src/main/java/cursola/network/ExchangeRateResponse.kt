package cursola.network

import java.util.Currency
import java.util.Date

data class ExchangeRateResponse(
    val currency: Currency,
    val rate: Double,
    val timestamp: Date
) {

    constructor(
        currency: String,
        rate: String,
        timestamp: Date
    ) : this(
        currency = Currency.getInstance(currency),
        rate = rate.toDouble(),
        timestamp = timestamp
    )

}