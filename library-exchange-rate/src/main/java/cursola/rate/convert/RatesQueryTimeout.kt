package cursola.rate.convert

import cursola.rate.ExchangeRateError
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Currency
import java.util.Locale
import kotlin.time.Duration

class RatesQueryTimeout(
    private val origin: RatesQuery,
    private val timeout: Duration
) : RatesQuery {

    override suspend fun currencies(locale: Locale) = bounded { origin.currencies(locale) }

    override suspend fun latest(base: Currency) = bounded { origin.latest(base) }

    private suspend fun <T : Any> bounded(block: suspend () -> T): T =
        withTimeoutOrNull(timeout) { block() }
            ?: throw ExchangeRateError.NotFoundException("Rates query did not finish within $timeout")

}
