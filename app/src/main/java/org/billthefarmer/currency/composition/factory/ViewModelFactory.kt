package org.billthefarmer.currency.composition.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.composition.scope.CompositionScope

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