package org.billthefarmer.currency.screen.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.billthefarmer.currency.presentation.model.DaySnapshot
import java.util.*

class DetailViewModel(
    val currency: Currency
) : ViewModel() {
    val rates = MutableStateFlow<List<DaySnapshot>>(emptyList())
}