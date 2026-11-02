package cursola.rate.convert

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateError
import cursola.rate.FavoriteDataSource
import kotlinx.coroutines.awaitCancellation
import java.math.BigDecimal
import java.util.Currency
import java.util.Date

internal val EUR: Currency = Currency.getInstance("EUR")
internal val USD: Currency = Currency.getInstance("USD")
internal val CZK: Currency = Currency.getInstance("CZK")
internal val GBP: Currency = Currency.getInstance("GBP")
internal val JPY: Currency = Currency.getInstance("JPY")

internal fun money(amount: String, currency: Currency) = Money(BigDecimal(amount), currency)

internal class TableConversionRates(private val eurBased: Map<Currency, Double>) : ConversionRateDataSource {
    override suspend fun get(from: Currency, to: Currency): Double {
        val fromRate = eurBased[from] ?: throw ExchangeRateError.NotFoundException("no $from")
        val toRate = eurBased[to] ?: throw ExchangeRateError.NotFoundException("no $to")
        return toRate / fromRate
    }
}

internal class ListFavorites(private val items: List<Currency>) : FavoriteDataSource {
    override suspend fun list() = items
    override suspend fun add(currency: Currency, priority: Int) = error("read only")
    override suspend fun remove(currency: Currency) = error("read only")
}

internal class ListExchangeRates(private val items: List<ExchangeRate>) : ExchangeRateDataSource {
    override suspend fun get() = items
}

internal class FixedTimestamp(private val date: Date) : RatesTimestamp {
    override suspend fun get() = date
}

internal class HangingConverter : CurrencyConverter {
    override suspend fun convert(amount: Money, to: Currency): Conversion = awaitCancellation()
    override suspend fun convertToFavorites(amount: Money): List<Conversion> = awaitCancellation()
}

internal class HangingRatesQuery : RatesQuery {
    override suspend fun currencies(locale: java.util.Locale): List<CurrencyInfo> = awaitCancellation()
    override suspend fun latest(base: Currency): List<Conversion> = awaitCancellation()
}
