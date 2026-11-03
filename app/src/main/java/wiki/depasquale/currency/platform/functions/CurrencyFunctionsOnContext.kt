package wiki.depasquale.currency.platform.functions

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CurrencyFunctionsOnContext(
    private val origin: CurrencyFunctions,
    private val context: CoroutineContext
) : CurrencyFunctions {

    override suspend fun convert(amount: Double, from: String, to: String) =
        withContext(context) { origin.convert(amount, from, to) }

    override suspend fun convertToFavorites(amount: Double, from: String) =
        withContext(context) { origin.convertToFavorites(amount, from) }

    override suspend fun listCurrencies() =
        withContext(context) { origin.listCurrencies() }

    override suspend fun latestRates(base: String) =
        withContext(context) { origin.latestRates(base) }

}
