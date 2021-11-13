package org.billthefarmer.composition.tooling

import org.billthefarmer.composition.core.Alias
import org.billthefarmer.composition.core.CompositionScope

fun getNoopScope(): CompositionScope {
    return object : CompositionScope {
        override fun <T> get(type: Class<T>, alias: Alias?): T {
            throw NotImplementedError()
        }
    }
}