package wiki.depasquale.currency.platform.functions

import androidx.appfunctions.AppFunctionSerializable

/**
 * The latest exchange rate from a base currency into another currency.
 *
 * @property base The ISO 4217 code of the base currency the rate starts from, for example "EUR".
 * @property code The ISO 4217 code of the currency the rate converts into, for example "USD".
 * @property rate How many units of this currency one unit of the base currency buys.
 * @property timestampMillis When the exchange rate was published, in milliseconds since the Unix epoch, or null
 * when unknown.
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
class RateResult(
    val base: String,
    val code: String,
    val rate: Double,
    val timestampMillis: Long?
)
