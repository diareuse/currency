package cursola.network

import java.text.ChoiceFormat
import java.util.Currency
import java.util.Date
import kotlin.random.Random

fun makeResponse(
    currency: Currency = Currency.getAvailableCurrencies().random(),
    rate: Double = ChoiceFormat.nextDouble(0.0, true) * Random.nextInt(0, 1000),
    timestamp: Date = Date(0)
) = ExchangeRateResponse(
    currency = currency,
    rate = rate,
    timestamp = timestamp
)
