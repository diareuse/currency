package wiki.depasquale.currency.composition.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.composition.scope.CompositionScope

class ViewModelFactory<T>(
    private val scope: CompositionScope,
    private val type: Class<T>,
    private val alias: Alias? = null,
    private val parameters: Array<out Any?> = emptyArray()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return scope.get(type, alias, parameters) as T
    }

}