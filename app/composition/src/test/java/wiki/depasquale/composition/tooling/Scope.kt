package wiki.depasquale.composition.tooling

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.composition.scope.CompositionScope

fun getNoopScope(): CompositionScope {
    return object : CompositionScope {
        override fun <T> get(type: Class<T>, alias: Alias?, params: Array<out Any?>): T {
            throw NotImplementedError()
        }
    }
}