package cursola.rate.convert

import cursola.rate.ExchangeRateError
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Currency
import kotlin.time.Duration

class CurrencyConverterTimeout(
    private val origin: CurrencyConverter,
    private val timeout: Duration
) : CurrencyConverter {

    override suspend fun convert(amount: Money, to: Currency) =
        bounded { origin.convert(amount, to) }

    override suspend fun convertToFavorites(amount: Money) =
        bounded { origin.convertToFavorites(amount) }

    private suspend fun <T : Any> bounded(block: suspend () -> T): T =
        withTimeoutOrNull(timeout) { block() }
            ?: throw ExchangeRateError.NotFoundException("Conversion did not finish within $timeout")

}
