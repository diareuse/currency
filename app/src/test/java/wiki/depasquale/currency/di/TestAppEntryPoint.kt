package wiki.depasquale.currency.di

import android.content.Context
import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateDataSource
import cursola.rate.HistoryDataSource
import spike.EntryPoint
import spike.Include
import spike.test.Replaces
import java.util.Currency
import java.util.Date

@EntryPoint
interface TestAppEntryPoint : AppEntryPoint {

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): TestAppEntryPoint
    }

    companion object

}

@Include
@Replaces(ExchangeRateDataSource::class)
class FixedExchangeRateDataSource : ExchangeRateDataSource {

    override suspend fun get() = listOf(rate)

    companion object {
        val rate = ExchangeRate(Currency.getInstance("XAU"), 0.42, Date(0))
    }

}

@Include
@Replaces(ConversionRateDataSource::class)
class IdentityConversionRateDataSource : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency) = 1.0

}

@Include
@Replaces(HistoryDataSource::class)
class EmptyHistoryDataSource : HistoryDataSource {

    override suspend fun get(currency: Currency) = emptyList<ExchangeRate>()

}
