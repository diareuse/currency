package org.billthefarmer.currency.ui.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.billthefarmer.currency.presentation.model.CurrencyModel

class DashboardViewModel : ViewModel() {

    val currencies = MutableStateFlow<List<CurrencyModel>>(emptyList())
    val selectedCurrency = MutableStateFlow<CurrencyModel?>(null)
    val amount = MutableStateFlow("")

}