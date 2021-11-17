package org.billthefarmer.composition.tooling

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.composition.scope.CompositionScope

fun getNoopScope(): CompositionScope {
    return object : CompositionScope {
        override fun <T> get(type: Class<T>, alias: Alias?, params: Array<out Any?>): T {
            throw NotImplementedError()
        }
    }
}