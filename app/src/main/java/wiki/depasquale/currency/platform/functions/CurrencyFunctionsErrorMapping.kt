package wiki.depasquale.currency.platform.functions

import androidx.appfunctions.AppFunctionElementNotFoundException
import androidx.appfunctions.AppFunctionInvalidArgumentException
import cursola.rate.ExchangeRateError

class CurrencyFunctionsErrorMapping(
    private val origin: CurrencyFunctions
) : CurrencyFunctions {

    override suspend fun convert(amount: Double, from: String, to: String) =
        mapped { origin.convert(amount, from, to) }

    override suspend fun convertToFavorites(amount: Double, from: String) =
        mapped { origin.convertToFavorites(amount, from) }

    override suspend fun listCurrencies() =
        mapped { origin.listCurrencies() }

    override suspend fun latestRates(base: String) =
        mapped { origin.latestRates(base) }

    private suspend fun <T> mapped(block: suspend () -> T): T = try {
        block()
    } catch (e: InvalidFunctionArgument) {
        throw AppFunctionInvalidArgumentException(e.message)
    } catch (e: ExchangeRateError.NotFoundException) {
        throw AppFunctionElementNotFoundException(e.message)
    }

}
