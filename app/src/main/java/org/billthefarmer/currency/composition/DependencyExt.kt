package org.billthefarmer.currency.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.composition.scope.get
import org.billthefarmer.currency.composition.factory.ViewModelFactory

inline fun <reified T : Any> composed(
    alias: Alias? = null,
    vararg params: Any?
): T {
    return Dependency().get(alias, params = params)
}


inline fun <reified T : Any> composedLazy(
    alias: Alias? = null,
    vararg params: Any?
): Lazy<T> {
    return lazy { composed(alias, params = params) }
}


@Composable
inline fun <reified T : Any> rememberComposed(
    alias: Alias? = null,
    vararg params: Any?
): T = remember(alias) {
    Dependency().get(alias, params = params)
}


@Composable
inline fun <reified T : ViewModel> composedViewModel(
    screen: String,
    alias: Alias? = null,
    vararg params: Any?
): T {
    val factory = remember(screen, alias, params) {
        ViewModelFactory(
            scope = Dependency(),
            type = T::class.java,
            alias = alias,
            parameters = params
        )
    }
    return viewModel(key = screen, factory = factory)
}