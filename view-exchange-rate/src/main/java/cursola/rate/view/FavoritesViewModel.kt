package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.ConversionRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.view.util.repeatingFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Currency
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FavoritesViewModel @Inject internal constructor(
    private val conversion: ConversionRateDataSource,
    private val favorite: FavoriteDataSource,
    private val latest: LatestValueDataSource
) : ViewModel() {

    val value = MutableStateFlow(latest.value)
    val selected = MutableStateFlow(Currency.getInstance(latest.currency))

    private val favorites = repeatingFlow(every = 10.seconds) { favorite.list() }
        .distinctUntilChanged()

    val items = combine(value, selected, favorites, ::transform)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch { value.debounce(5.seconds).collect { latest.value = it } }
        viewModelScope.launch { selected.collect { latest.currency = it.currencyCode } }
    }

    private suspend fun transform(
        value: String,
        selected: Currency,
        favorites: List<Currency>
    ) = buildList {
        for (favorite in favorites) {
            if (favorite == selected) continue
            val valueNumber = value.toDoubleOrNull() ?: 0.0
            val conversionRate = conversion.get(selected, favorite)
            val converted = ConvertedCurrency(favorite, conversionRate * valueNumber, true)
            add(converted)
        }
    }

}