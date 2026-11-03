package wiki.depasquale.currency.platform.functions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import wiki.depasquale.currency.di.appGraph

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@AppFunctionServiceEntryPoint(
    serviceName = "CurrencyAppFunctionsService",
    appFunctionXmlFileName = "currency_functions"
)
abstract class CurrencyAppFunctions : AppFunctionService() {

    private val functions get() = appGraph.currencyFunctions

    /**
     * Converts an amount of money from one currency into another with the exchange rates stored in the Cursola app.
     * Use it when the user asks how much an amount is worth in another currency.
     *
     * @param amount The amount to convert, in the source currency. Zero or more.
     * @param from The ISO 4217 code of the source currency, for example "EUR".
     * @param to The ISO 4217 code of the target currency, for example "USD".
     * @return The converted amount with the rate used and a display-ready text.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun convert(amount: Double, from: String, to: String): ConversionResult =
        functions.convert(amount, from, to)

    /**
     * Converts an amount of money into every currency the user marked as a favorite in the Cursola app, most
     * important favorite first. Use it when the user asks what an amount is worth without naming a target currency.
     * The source currency and favorites without an exchange rate are left out.
     *
     * @param amount The amount to convert, in the source currency. Zero or more.
     * @param from The ISO 4217 code of the source currency, for example "EUR".
     * @return One conversion per favorite currency; empty when the user has no other favorites.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun convertToFavorites(amount: Double, from: String): List<ConversionResult> =
        functions.convertToFavorites(amount, from)

    /**
     * Lists every currency the Cursola app has an exchange rate for, with names and symbols in the user's language.
     * Use it to find the ISO 4217 code of a currency the user named.
     *
     * @return The currencies that can be passed to the other currency functions.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun listCurrencies(): List<CurrencyInfoResult> =
        functions.listCurrencies()

    /**
     * Lists the latest exchange rates from a base currency into every other currency the Cursola app knows.
     * Use it when the user asks for current exchange rates.
     *
     * @param base The ISO 4217 code of the base currency, for example "EUR".
     * @return One rate per other currency.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun latestRates(base: String): List<RateResult> =
        functions.latestRates(base)

}
