package wiki.depasquale.currency.platform.functions

import androidx.appfunctions.AppFunctionSerializable

/**
 * A currency that Cursola has an exchange rate for.
 *
 * @property code The ISO 4217 code of the currency, for example "JPY". Pass it to the other currency functions.
 * @property displayName The name of the currency in the user's language, for example "Japanese Yen".
 * @property symbol The symbol of the currency in the user's language, for example "¥".
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
class CurrencyInfoResult(
    val code: String,
    val displayName: String,
    val symbol: String
)
