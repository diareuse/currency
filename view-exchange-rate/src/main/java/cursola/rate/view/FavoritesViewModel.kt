package cursola.rate.view

import androidx.lifecycle.ViewModel
import cursola.rate.ConversionRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.view.util.repeatingFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.Currency
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FavoritesViewModel @Inject internal constructor(
    private val conversion: ConversionRateDataSource,
    private val favorite: FavoriteDataSource
) : ViewModel() {

    val value = MutableStateFlow(1.0)
    val selected = MutableStateFlow(Currency.getInstance("EUR"))

    private val favorites = repeatingFlow(every = 10.seconds) { favorite.list() }
        .distinctUntilChanged()

    val items = combine(value, selected, favorites, ::transform)

    private suspend fun transform(
        value: Double,
        selected: Currency,
        favorites: List<Currency>
    ) = buildList {
        for (favorite in favorites) {
            if (favorite == selected) continue
            val conversionRate = conversion.get(selected, favorite)
            val converted = ConvertedCurrency(favorite, conversionRate * value, true)
            add(converted)
        }
    }

}