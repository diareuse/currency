package org.billthefarmer.currency.ui.selection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.billthefarmer.currency.presentation.model.CurrencyModel

class SelectionViewModel : ViewModel() {

    val currencies = MutableStateFlow<List<CurrencyModel>>(emptyList())
    val pendingCurrencies = MutableStateFlow<List<CurrencyModel>>(emptyList())

    fun addPending(model: CurrencyModel) {
        synchronized(pendingCurrencies) {
            val currentList = pendingCurrencies.value.toMutableList()
            currentList += model
            pendingCurrencies.value = currentList
        }
    }

    fun removePending(model: CurrencyModel) {
        synchronized(pendingCurrencies) {
            val currentList = pendingCurrencies.value.toMutableList()
            currentList -= model
            pendingCurrencies.value = currentList
        }
    }

}