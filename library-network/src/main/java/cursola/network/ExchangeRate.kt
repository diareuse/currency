package cursola.network

import cursola.core.ExchangeRate

internal fun ExchangeRate(
    response: ExchangeRateResponse
) = ExchangeRate(
    rate = response.rate,
    timestamp = response.timestamp
)