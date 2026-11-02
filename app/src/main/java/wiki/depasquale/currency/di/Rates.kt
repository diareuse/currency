package wiki.depasquale.currency.di

import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.RatesQuery
import spike.Include
import wiki.depasquale.currency.platform.rates.ConversionsTable
import wiki.depasquale.currency.platform.rates.CurrenciesTable
import wiki.depasquale.currency.platform.rates.LatestRatesTable
import wiki.depasquale.currency.platform.rates.RatesTable
import wiki.depasquale.currency.platform.rates.RatesTableErrorBoundary
import wiki.depasquale.currency.platform.rates.RatesTableRouter
import wiki.depasquale.currency.platform.rates.RatesTableTimeout
import wiki.depasquale.cursola.contract.CurrencyContract.Conversions
import wiki.depasquale.cursola.contract.CurrencyContract.Currencies
import wiki.depasquale.cursola.contract.CurrencyContract.LatestRates
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class BinderTimeout(val value: Duration)

@Include
internal fun binderTimeout() = BinderTimeout(3.seconds)

@Include
internal fun ratesTable(
    rates: RatesQuery,
    converter: CurrencyConverter,
    timeout: BinderTimeout
): RatesTable {
    val router = RatesTableRouter(
        mapOf(
            Currencies.PATH to CurrenciesTable(rates, Locale.getDefault()),
            LatestRates.PATH to LatestRatesTable(rates),
            Conversions.PATH to ConversionsTable(converter),
        )
    )
    return RatesTableTimeout(RatesTableErrorBoundary(router), timeout.value)
}
