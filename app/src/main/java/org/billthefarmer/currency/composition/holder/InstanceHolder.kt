package org.billthefarmer.currency.composition.holder

import org.billthefarmer.currency.composition.core.Alias

interface InstanceHolder<Type, Value> {

    fun get(type: Type, alias: Alias? = null): Value?
    fun getKeys(): Map<Type, List<Alias?>>

}