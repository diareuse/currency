package wiki.depasquale.currency.di

import androidx.lifecycle.ViewModel
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.convert.CurrencyConverter
import cursola.rate.view.FavoritesViewModel
import cursola.rate.view.HistoricalViewModel
import cursola.rate.view.ListingViewModel
import cursola.rate.view.spikeViewModelFavoritesViewModel
import cursola.rate.view.spikeViewModelHistoricalViewModel
import cursola.rate.view.spikeViewModelListingViewModel
import spike.BindTarget
import spike.Include
import spike.lifecycle.viewmodel.ViewModelKey

@Include(bindTo = BindTarget.Map)
@ViewModelKey(FavoritesViewModel::class)
internal fun favoritesViewModel(
    converter: CurrencyConverter,
    favorite: FavoriteDataSource,
    latest: LatestValueDataSource
): ViewModel = spikeViewModelFavoritesViewModel(converter, favorite, latest)

@Include(bindTo = BindTarget.Map)
@ViewModelKey(HistoricalViewModel::class)
internal fun historicalViewModel(
    favorite: FavoriteDataSource,
    history: HistoryDataSource
): ViewModel = spikeViewModelHistoricalViewModel(favorite, history)

@Include(bindTo = BindTarget.Map)
@ViewModelKey(ListingViewModel::class)
internal fun listingViewModel(
    exchange: ExchangeRateDataSource,
    favorite: FavoriteDataSource
): ViewModel = spikeViewModelListingViewModel(exchange, favorite)
