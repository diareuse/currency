package org.billthefarmer.currency.composition.holder

import org.billthefarmer.currency.composition.core.Alias

interface MutableInstanceHolder<Type, Value> : InstanceHolder<Type, Value> {

    fun set(type: Type, alias: Alias?, value: Value)

}