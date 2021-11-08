package org.billthefarmer.currency.composition.tooling

import org.billthefarmer.currency.composition.core.Alias
import org.billthefarmer.currency.composition.core.CompositionScope

fun getNoopScope(): CompositionScope {
    return object : CompositionScope {
        override fun <T> get(type: Class<T>, alias: Alias?): T {
            throw NotImplementedError()
        }
    }
}