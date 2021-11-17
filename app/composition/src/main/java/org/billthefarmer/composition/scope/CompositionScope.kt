package org.billthefarmer.composition.scope

import org.billthefarmer.composition.extra.Alias

interface CompositionScope {

    fun <T> get(type: Class<T>, alias: Alias? = null, params: Array<out Any?> = emptyArray()): T

}