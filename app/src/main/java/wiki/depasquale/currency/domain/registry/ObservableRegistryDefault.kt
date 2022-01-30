package wiki.depasquale.currency.domain.registry

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ObservableRegistryDefault<T> : ObservableRegistry<T> {

    private val state = MutableStateFlow<List<T>>(emptyList())

    override fun get(): StateFlow<List<T>> {
        return state.asStateFlow()
    }

    override fun add(items: List<T>) = state.update {
        it + items
    }

    override fun remove(items: List<T>) = state.update {
        it - items
    }

}