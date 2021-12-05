package org.billthefarmer.currency.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.registry.add
import org.billthefarmer.currency.domain.registry.observableRegistryOf
import org.billthefarmer.currency.presentation.model.CurrencyModel

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