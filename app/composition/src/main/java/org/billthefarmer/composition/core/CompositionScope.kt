package org.billthefarmer.composition.core

interface CompositionScope {

    fun <T> get(type: Class<T>, alias: Alias? = null, params: Array<out Any?> = emptyArray()): T

}

inline fun <reified T> CompositionScope.get(alias: Alias? = null, vararg params: Any?): T {
    return get(T::class.java, alias, params)
}