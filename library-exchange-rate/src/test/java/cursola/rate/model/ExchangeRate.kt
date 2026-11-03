package cursola.rate.model

import cursola.rate.ExchangeRate
import cursola.rate.util.nextCurrency
import java.util.Currency
import java.util.Date
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

fun makeExchangeRate(
    currency: Currency = nextCurrency(),
    rate: Double = nextDouble(0.001, 1.0) * nextInt(1, 1000),
    timestamp: Date = Date(0)
) = ExchangeRate(
    currency = currency,
    rate = rate,
    timestamp = timestamp
)
