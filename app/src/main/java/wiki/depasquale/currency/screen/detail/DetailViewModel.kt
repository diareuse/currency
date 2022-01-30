package wiki.depasquale.currency.screen.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import wiki.depasquale.currency.presentation.model.DaySnapshot
import java.util.*

class DetailViewModel(
    val currency: Currency
) : ViewModel() {
    val rates = MutableStateFlow<List<DaySnapshot>>(emptyList())
}