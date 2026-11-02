package cursola.rate.convert

import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateError
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

class RatesQueryDefault(
    private val source: ExchangeRateDataSource
) : RatesQuery {

    override suspend fun currencies(locale: Locale): List<CurrencyInfo> = snapshot().map {
        CurrencyInfo(it.currency, it.currency.getDisplayName(locale), it.currency.getSymbol(locale))
    }

    override suspend fun latest(base: Currency): List<Conversion> {
        val rates = snapshot()
        val baseRate = rates.firstOrNull { it.currency == base }
            ?: throw ExchangeRateError.NotFoundException("No exchange rate for $base")
        val timestamp = rates.maxOf { it.timestamp }
        val source = Money(BigDecimal.ONE, base)
        return rates.filter { it.currency != base }.map {
            val rate = it.rate / baseRate.rate
            Conversion(source, Money(BigDecimal.valueOf(rate), it.currency), rate, timestamp)
        }
    }

    private suspend fun snapshot(): List<ExchangeRate> {
        val rates = source.get()
        if (rates.isEmpty() || rates.any { it.currency == euro }) return rates
        return listOf(ExchangeRate(euro, 1.0, rates.maxOf { it.timestamp })) + rates
    }

    private companion object {
        val euro: Currency = Currency.getInstance("EUR")
    }

}
