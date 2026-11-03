package wiki.depasquale.currency.platform.functions

import cursola.rate.convert.Conversion
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.Money
import cursola.rate.convert.MoneyFormat
import cursola.rate.convert.RatesQuery
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

class CurrencyFunctionsDefault(
    private val converter: CurrencyConverter,
    private val rates: RatesQuery,
    private val format: MoneyFormat,
    private val locale: Locale
) : CurrencyFunctions {

    override suspend fun convert(amount: Double, from: String, to: String): ConversionResult =
        converter.convert(money(amount, from), currency(to)).toResult()

    override suspend fun convertToFavorites(amount: Double, from: String): List<ConversionResult> {
        val source = money(amount, from)
        val conversions = converter.convertToFavorites(source)
        if (conversions.isEmpty()) rates.latest(source.currency)
        return conversions.map { it.toResult() }
    }

    override suspend fun listCurrencies(): List<CurrencyInfoResult> = rates.currencies(locale).map {
        CurrencyInfoResult(it.currency.currencyCode, it.displayName, it.symbol)
    }

    override suspend fun latestRates(base: String): List<RateResult> = rates.latest(currency(base)).map {
        RateResult(it.source.currency.currencyCode, it.result.currency.currencyCode, it.rate, it.timestamp?.time)
    }

    private fun money(amount: Double, code: String): Money {
        if (!amount.isFinite() || amount < 0) {
            throw InvalidFunctionArgument("Amount must be a finite number of zero or more, was $amount")
        }
        return Money(BigDecimal.valueOf(amount), currency(code))
    }

    private fun currency(code: String): Currency = try {
        Currency.getInstance(code.trim().uppercase(Locale.ROOT))
    } catch (_: IllegalArgumentException) {
        throw InvalidFunctionArgument("\"$code\" is not an ISO 4217 currency code")
    }

    private fun Conversion.toResult() = ConversionResult(
        amount = source.amount.toDouble(),
        from = source.currency.currencyCode,
        result = result.amount.toDouble(),
        to = result.currency.currencyCode,
        rate = rate,
        formatted = format.format(result, locale),
        timestampMillis = timestamp?.time
    )

}
