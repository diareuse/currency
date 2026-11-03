package wiki.depasquale.currency.di

import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.CurrencyConverterTimeout
import cursola.rate.convert.MoneyFormat
import cursola.rate.convert.RatesQuery
import cursola.rate.convert.RatesQueryTimeout
import kotlinx.coroutines.Dispatchers
import spike.Include
import wiki.depasquale.currency.platform.functions.CurrencyFunctions
import wiki.depasquale.currency.platform.functions.CurrencyFunctionsDefault
import wiki.depasquale.currency.platform.functions.CurrencyFunctionsErrorMapping
import wiki.depasquale.currency.platform.functions.CurrencyFunctionsOnContext
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class AppFunctionsDeadline(val value: Duration)

@Include
internal fun appFunctionsDeadline() = AppFunctionsDeadline(3.seconds)

@Include
internal fun currencyFunctions(
    converter: CurrencyConverter,
    rates: RatesQuery,
    format: MoneyFormat,
    deadline: AppFunctionsDeadline
): CurrencyFunctions {
    val boundedConverter = CurrencyConverterTimeout(converter, deadline.value)
    val boundedRates = RatesQueryTimeout(rates, deadline.value)
    val core = CurrencyFunctionsDefault(boundedConverter, boundedRates, format, Locale.getDefault())
    val offMain = CurrencyFunctionsOnContext(core, Dispatchers.IO)
    return CurrencyFunctionsErrorMapping(offMain)
}
