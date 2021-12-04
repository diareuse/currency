package org.billthefarmer.currency.screen.selection

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

    fun removePending(model: List<CurrencyModel>) {
        synchronized(pendingCurrencies) {
            val currentList = pendingCurrencies.value.toMutableList()
            currentList -= model
            pendingCurrencies.value = currentList
        }
        val currentList = currencies.value.toMutableList()
        currentList -= model
        currencies.value = currentList
    }

}