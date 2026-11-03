package wiki.depasquale.currency.platform.functions

import androidx.appfunctions.AppFunctionSerializable

/**
 * An amount of money converted from one currency into another with the exchange rates stored in Cursola.
 *
 * @property amount The amount that was converted, in the source currency.
 * @property from The ISO 4217 code of the source currency, for example "EUR".
 * @property result The converted amount in the target currency, not rounded.
 * @property to The ISO 4217 code of the target currency, for example "USD".
 * @property rate How many units of the target currency one unit of the source currency buys.
 * @property formatted The converted amount ready to show or read to the user, in the user's language, with the
 * currency symbol and rounded to the digits the target currency normally uses, for example "$108.42".
 * @property timestampMillis When the exchange rates used were published, in milliseconds since the Unix epoch, or
 * null when unknown.
 */
@Suppress("LongParameterList")
@AppFunctionSerializable(isDescribedByKDoc = true)
class ConversionResult(
    val amount: Double,
    val from: String,
    val result: Double,
    val to: String,
    val rate: Double,
    val formatted: String,
    val timestampMillis: Long?
)
