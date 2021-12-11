package org.billthefarmer.currency.composition

import android.util.Xml
import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.composition.scope.*
import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapterImpl
import org.billthefarmer.currency.domain.database.DatabaseStorage
import org.billthefarmer.currency.domain.model.ExchangeRatePreference
import org.billthefarmer.currency.domain.network.NetworkService
import org.billthefarmer.currency.domain.network.NetworkServiceImpl
import org.billthefarmer.currency.domain.preference.*
import org.billthefarmer.currency.domain.rate.*
import org.billthefarmer.currency.domain.time.TimeRangeFactory
import org.billthefarmer.currency.domain.time.TimeRangeFactoryLastWorkday
import org.billthefarmer.currency.domain.tooling.setToDayEnd
import org.billthefarmer.currency.domain.tooling.setToDayStart
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

val RatesNotSelected = Alias("rate-not-selected")
val RatesToday = Alias("rate-today")
val Rates90Days = Alias("rate-90-days")
val RatesAllTime = Alias("rate-all-time")

val ExchangeRateModel = Alias("exchange-rate-model")

val TimeRangeWorkday = Alias("time-range-workday")

fun CompositionScopeDefault.Builder.domainModule() = apply {
    single { SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) }
    single { Xml.newPullParser() }
    single<ExchangeRatesAdapter> { ExchangeRatesAdapterImpl(get(), get()) }

    single(ExchangeRateModel) { createSharedPreferenceProvider("exchange-rate") }
    factory(ExchangeRateModel) { createSharedPreferenceWriter(ExchangeRateModel) }
    factory(ExchangeRateModel) { createSharedPreferenceReader(ExchangeRateModel) }

    single { DatabaseStorage.create(get()) }
    single { get<DatabaseStorage>().getCurrencies() }
    single { get<DatabaseStorage>().getRates() }

    factory<NetworkService> { NetworkServiceImpl() }

    factory<TimeRangeFactory>(TimeRangeWorkday) { TimeRangeFactoryLastWorkday() }

    factory(RatesNotSelected) { createExchangeRatesNotSelected() }
    factory(RatesToday) { createExchangeRatesToday() }
    factory(Rates90Days) { createExchangeRates90Days() }
    factory(RatesAllTime) { createExchangeRatesAllTime() }
}

private fun CompositionScope.createSharedPreferenceWriter(alias: Alias): PreferenceWriter<ExchangeRatePreference> {
    return SharedPreferenceWriter(get(alias), ExchangeRatePreference::class)
}

private fun CompositionScope.createSharedPreferenceReader(alias: Alias): PreferenceReader<ExchangeRatePreference> {
    return SharedPreferenceReader(get(alias), ExchangeRatePreference::class)
}

private fun CompositionScope.createSharedPreferenceProvider(name: String): SharedPreferenceProvider {
    var result: SharedPreferenceProvider
    result = SharedPreferenceProviderImpl(get(), name)
    result = SharedPreferenceProviderCaching(result)
    return result
}

private fun CompositionScope.createExchangeRatesToday(): ExchangeRates {
    val url = URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml")
    return createExchangeRates(url, 0, get(TimeRangeWorkday))
}

private fun CompositionScope.createExchangeRatesNotSelected(): ExchangeRates {
    val url = URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml")
    var result = createExchangeRatesUnbiased(url, 0, get(TimeRangeWorkday))
    result = ExchangeRatesFilterNotSelected(result, get(ExchangeRateModel))
    return result
}

private fun CompositionScope.createExchangeRates90Days(): ExchangeRates {
    val url = URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml")
    return createExchangeRates(url, 58) {
        val start = Calendar.getInstance().setToDayStart()
            .also { it[Calendar.DAY_OF_YEAR] -= 90 }
            .timeInMillis
        val end = Calendar.getInstance().setToDayEnd().timeInMillis
        start..end
    }
}

private fun CompositionScope.createExchangeRatesAllTime(): ExchangeRates {
    val url = URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml")
    return createExchangeRates(url, 66) {
        val start = 0L
        val end = Calendar.getInstance().setToDayEnd().timeInMillis
        start..end
    }
}

private fun CompositionScope.createExchangeRatesUnbiased(
    url: URL,
    networkRequestAtCount: Int,
    factory: TimeRangeFactory
): ExchangeRates {
    var network: ExchangeRates = ExchangeRatesNetwork(url, get(), get())
    network = ExchangeRatesAppendBaseline(network)
    network = ExchangeRatesErrorDefault(ExchangeRatesSaving(network, get(), get(), get()), network)
    network = ExchangeRatesCache(network)

    var database: ExchangeRates = ExchangeRatesDatabase(get(), get(), factory)
    database = ExchangeRatesErrorDefault(database, ExchangeRatesEmpty())

    var result: ExchangeRates
    result = ExchangeRatesCountLessFork(database, network, networkRequestAtCount)
    result = ExchangeRatesSort(result)
    return result
}

private fun CompositionScope.createExchangeRates(
    url: URL,
    networkRequestAtCount: Int,
    factory: TimeRangeFactory
): ExchangeRates {
    var network = createExchangeRatesUnbiased(url, networkRequestAtCount, factory)
    network = ExchangeRatesFilterSelected(network, get(ExchangeRateModel))
    return network
}
