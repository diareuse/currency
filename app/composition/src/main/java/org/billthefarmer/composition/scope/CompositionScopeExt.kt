package org.billthefarmer.composition.scope

import org.billthefarmer.composition.extra.Alias

inline fun <reified T> CompositionScope.get(alias: Alias? = null, vararg params: Any?): T {
    return get(T::class.java, alias, params)
}