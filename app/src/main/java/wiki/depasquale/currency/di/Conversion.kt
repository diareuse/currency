package wiki.depasquale.currency.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.convert.AmountParser
import cursola.rate.convert.AmountParserDefault
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.CurrencyConverterDefault
import cursola.rate.convert.CurrencyTokenResolverFirstMatch
import cursola.rate.convert.CurrencyTokenResolverIso
import cursola.rate.convert.CurrencyTokenResolverSymbol
import cursola.rate.convert.RatesQuery
import cursola.rate.convert.RatesQueryDefault
import cursola.rate.convert.RatesTimestampExchangeRate
import spike.Include

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
