package wiki.depasquale.currency.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.convert.AmountParser
import cursola.rate.convert.AmountParserDefault
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.CurrencyConverterDefault
import cursola.rate.convert.CurrencyFractionDigitsPlatform
import cursola.rate.convert.CurrencyTokenResolverFirstMatch
import cursola.rate.convert.CurrencyTokenResolverIso
import cursola.rate.convert.CurrencyTokenResolverSymbol
import cursola.rate.convert.MoneyFormat
import cursola.rate.convert.MoneyFormatLocalized
import cursola.rate.convert.RatesQuery
import cursola.rate.convert.RatesQueryDefault
import cursola.rate.convert.RatesTimestampExchangeRate
import cursola.rate.convert.TextConversion
import cursola.rate.convert.TextConversionDefault
import cursola.rate.convert.TextConversionErrorBoundary
import cursola.rate.convert.TextConversionOnContext
import cursola.rate.convert.TextConversionTimeout
import kotlinx.coroutines.Dispatchers
import spike.Include
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Include
internal fun converter(
    conversion: ConversionRateDataSource,
    favorite: FavoriteDataSource,
    exchange: ExchangeRateDataSource
): CurrencyConverter = CurrencyConverterDefault(conversion, favorite, RatesTimestampExchangeRate(exchange))

@Include
internal fun ratesQuery(exchange: ExchangeRateDataSource): RatesQuery = RatesQueryDefault(exchange)

@Include
internal fun amountParser(): AmountParser = AmountParserDefault(
    CurrencyTokenResolverFirstMatch(listOf(CurrencyTokenResolverIso(), CurrencyTokenResolverSymbol()))
)

@Include
internal fun moneyFormat(): MoneyFormat = MoneyFormatLocalized(CurrencyFractionDigitsPlatform())

internal data class TextConversionDeadline(val value: Duration)

@Include
internal fun textConversionDeadline() = TextConversionDeadline(5.seconds)

@Include
internal fun textConversion(
    parser: AmountParser,
    favorite: FavoriteDataSource,
    converter: CurrencyConverter,
    deadline: TextConversionDeadline
): TextConversion {
    val core = TextConversionDefault(parser, favorite, converter)
    val bounded = TextConversionTimeout(core, deadline.value)
    val offMain = TextConversionOnContext(bounded, Dispatchers.IO)
    return TextConversionErrorBoundary(offMain)
}
