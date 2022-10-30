package cursola.rate.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cursola.rate.ExchangeRate
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.util.Currency
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject internal constructor(
    private val favorite: FavoriteDataSource,
    private val history: HistoryDataSource
) : ViewModel() {

    private val favorites = flow { emit(favorite.list()) }

    private val emptyValues = favorites
        .map(::toEmptyHistoryValues)

    private val actualValues = favorites
        .map(::toHistoryValues)
        .onStart { emit(emptyList()) }

    val items = combine(emptyValues, actualValues) { empty, actual ->
        actual.ifEmpty { empty }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private suspend fun toHistoryValues(currencies: List<Currency>): List<HistoryValues> {
        return currencies.map { HistoryValues(it, history.get(it).map(::ChartValue)) }
    }

    private fun toEmptyHistoryValues(currencies: List<Currency>): List<HistoryValues> {
        return currencies.map { HistoryValues(it, emptyList()) }
    }

    data class HistoryValues(
        val currency: Currency,
        val values: List<ChartValue>
    ) {

        val min get() = values.minByOrNull { it.y }?.y ?: 0.0
        val max get() = values.minByOrNull { it.y }?.y ?: 0.0

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
