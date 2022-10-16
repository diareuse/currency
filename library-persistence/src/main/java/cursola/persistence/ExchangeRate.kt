package cursola.persistence

import cursola.core.ExchangeRate

internal fun ExchangeRate(
    value: ExchangeRateSimple
) = ExchangeRate(
    rate = value.rate,
    timestamp = value.timestamp
)