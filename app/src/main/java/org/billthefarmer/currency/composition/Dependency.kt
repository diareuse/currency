package org.billthefarmer.currency.composition

import org.billthefarmer.composition.core.Alias
import org.billthefarmer.composition.core.buildCompositor
import org.billthefarmer.composition.core.get

object Dependency {

    val compositor = buildCompositor {
        domainModule()
        presentationModule()
        uiModule()
    }

}

inline fun <reified T : Any> composed(alias: Alias? = null): T {
    return Dependency.compositor.get(alias)
}

inline fun <reified T : Any> composedLazy(alias: Alias?): Lazy<T> {
    return lazy { composed(alias) }
}