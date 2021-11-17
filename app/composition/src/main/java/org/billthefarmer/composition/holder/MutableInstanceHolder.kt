package org.billthefarmer.composition.holder

import org.billthefarmer.composition.extra.Alias

interface MutableInstanceHolder<Type, Value> : InstanceHolder<Type, Value> {

    fun set(type: Type, alias: Alias?, value: Value)

}