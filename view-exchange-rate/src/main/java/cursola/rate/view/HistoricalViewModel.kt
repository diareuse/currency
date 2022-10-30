package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.ExchangeRate
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.di.ScopeSinceInception
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Currency
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject internal constructor(
    private val favorite: FavoriteDataSource,
    @ScopeSinceInception
    private val history: HistoryDataSource
) : ViewModel() {

    val items = flow { emit(favorite.list()) }
        .distinctUntilChanged()
        .map(::toHistoryValues)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private suspend fun toHistoryValues(currencies: List<Currency>): List<HistoryValues> {
        return currencies.map { HistoryValues(it, history.get(it).map(::ChartValue)) }
    }

    data class HistoryValues(
        val currency: Currency,
        val values: List<ChartValue>
    ) {

        val min get() = values.minBy { it.y }.y
        val max get() = values.maxBy { it.y }.y

    }

    data class ChartValue(
        val x: Date,
        val y: Double
    ) {

        constructor(
            rate: ExchangeRate
        ) : this(
            rate.timestamp,
            rate.rate
        )

    }

}
