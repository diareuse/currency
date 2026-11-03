package wiki.depasquale.currency.platform.widget

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import spike.Include
import spike.Singleton

@Singleton
@Include
class FavoritesWidgetRevision {

    private val state = MutableStateFlow(0L)

    val current: StateFlow<Long> = state.asStateFlow()

    fun bump() {
        state.update { it + 1 }
    }

}
