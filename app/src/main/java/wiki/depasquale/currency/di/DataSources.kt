package wiki.depasquale.currency.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.di.ExchangeRateLibrary
import spike.Include
import wiki.depasquale.currency.platform.FavoriteDataSourceNotifying
import wiki.depasquale.currency.platform.FavoritesChangedListener

@Include
internal fun conversionRate(@ExchangeRateLibrary source: ConversionRateDataSource): ConversionRateDataSource = source

@Include
internal fun exchangeRate(@ExchangeRateLibrary source: ExchangeRateDataSource): ExchangeRateDataSource = source

@Include
internal fun favorite(
    @ExchangeRateLibrary source: FavoriteDataSource,
    changed: FavoritesChangedListener
): FavoriteDataSource = FavoriteDataSourceNotifying(source, changed)

@Include
internal fun history(@ExchangeRateLibrary source: HistoryDataSource): HistoryDataSource = source

@Include
internal fun latestValue(@ExchangeRateLibrary source: LatestValueDataSource): LatestValueDataSource = source
