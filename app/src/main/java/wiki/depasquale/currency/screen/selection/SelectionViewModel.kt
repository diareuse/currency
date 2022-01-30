package wiki.depasquale.currency.screen.selection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import wiki.depasquale.currency.domain.registry.add
import wiki.depasquale.currency.domain.registry.observableRegistryOf
import wiki.depasquale.currency.presentation.model.CurrencyModel

class SelectionViewModel : ViewModel() {

    private val _pendingCurrencies = observableRegistryOf<CurrencyModel>()

    val currencies = MutableStateFlow<List<CurrencyModel>>(emptyList())
    val pendingCurrencies = _pendingCurrencies.get()

    fun addPending(model: CurrencyModel) {
        _pendingCurrencies.add(model)
    }

    fun removePending(model: List<CurrencyModel>) {
        _pendingCurrencies.remove(model)
        currencies.update {
            it - model
        }
    }

}