package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.LatestValueDataSource
import spike.Export

@Export
interface ExchangeRateExport {
    @ExchangeRateLibrary
    val conversionRate: ConversionRateDataSource
    @ExchangeRateLibrary
    val exchangeRate: ExchangeRateDataSource
    @ExchangeRateLibrary
    val favorite: FavoriteDataSource
    @ExchangeRateLibrary
    val history: HistoryDataSource
    @ExchangeRateLibrary
    val latestValue: LatestValueDataSource
}
