package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.FavoriteDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.Money
import cursola.rate.view.util.repeatingFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import spike.lifecycle.viewmodel.SpikeViewModel
import java.math.BigDecimal
import java.util.Currency
import kotlin.time.Duration.Companion.seconds

@SpikeViewModel
class FavoritesViewModel internal constructor(
    private val converter: CurrencyConverter,
    private val favorite: FavoriteDataSource,
    private val latest: LatestValueDataSource
) : ViewModel() {

    val value = MutableStateFlow(latest.value)
    val selected = MutableStateFlow(Currency.getInstance(latest.currency))

    private val favorites = repeatingFlow(every = 10.seconds) { favorite.list() }
        .distinctUntilChanged()

    val items = combine(value, selected, favorites) { amount, currency, _ -> transform(amount, currency) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch { value.debounce(5.seconds).collect { latest.value = it } }
        viewModelScope.launch { selected.collect { latest.currency = it.currencyCode } }
    }

    private suspend fun transform(value: String, selected: Currency): List<ConvertedCurrency> {
        val amount = Money(value.toBigDecimalOrNull() ?: BigDecimal.ZERO, selected)
        return converter.convertToFavorites(amount).map {
            ConvertedCurrency(it.result.currency, it.result.amount.toDouble(), true)
        }
    }

}