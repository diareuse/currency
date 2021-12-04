package org.billthefarmer.currency.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel

class DashboardViewModel : ViewModel() {

    val currencyPivot = MutableStateFlow(ExchangeRate.Default)
    val currencies = MutableStateFlow<List<CurrencyModel>>(emptyList())
    val selectedCurrency = MutableStateFlow<CurrencyModel?>(null)
    val amount = MutableStateFlow("")
    val amountDouble = amount
        .map { it.toDoubleOrNull() ?: 1.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1.0)

}