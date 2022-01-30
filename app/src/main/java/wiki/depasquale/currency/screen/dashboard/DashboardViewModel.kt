package wiki.depasquale.currency.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.registry.add
import wiki.depasquale.currency.domain.registry.observableRegistryOf
import wiki.depasquale.currency.presentation.model.CurrencyModel

class DashboardViewModel : ViewModel() {

    private val _pendingDeletions = observableRegistryOf<CurrencyModel>()

    val pendingDeletions = _pendingDeletions.get()
    val currencyPivot = MutableStateFlow(ExchangeRate.Default)
    val currencies = MutableStateFlow<List<CurrencyModel>>(emptyList())
    val selectedCurrency = MutableStateFlow<CurrencyModel?>(null)
    val amount = MutableStateFlow("")
    val amountDouble = amount
        .map { it.toDoubleOrNull() ?: 1.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1.0)

    fun addPendingDeletion(item: CurrencyModel) {
        _pendingDeletions.add(item)
    }

    fun removePendingDeletion(items: List<CurrencyModel>) {
        _pendingDeletions.remove(items)
        currencies.update {
            it - items
        }
    }

}