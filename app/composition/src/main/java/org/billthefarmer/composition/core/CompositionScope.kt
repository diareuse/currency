package org.billthefarmer.composition.core

interface CompositionScope {

    fun <T> get(type: Class<T>, alias: Alias? = null): T

}

inline fun <reified T> CompositionScope.get(alias: Alias? = null): T {
    return get(T::class.java, alias)
}