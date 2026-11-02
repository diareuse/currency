package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import spike.lifecycle.viewmodel.SpikeViewModel
import java.util.Currency

@SpikeViewModel
class ListingViewModel internal constructor(
    private val exchange: ExchangeRateDataSource,
    private val favorite: FavoriteDataSource
) : ViewModel() {

    private val trigger = MutableStateFlow(Any())

    val items: Flow<List<ConvertedCurrency>> = trigger
        .map {
            val favorites = favorite.list()
            exchange.get()
                .asSequence()
                .map { ConvertedCurrency(it, it.currency in favorites) }
                .sortedByDescending { it.isFavorite }
                .toList()
        }

    fun add(currency: Currency) = viewModelScope.launch {
        favorite.add(currency, 0)
        trigger.value = Any()
    }

    fun remove(currency: Currency) = viewModelScope.launch {
        favorite.remove(currency)
        trigger.value = Any()
    }

}