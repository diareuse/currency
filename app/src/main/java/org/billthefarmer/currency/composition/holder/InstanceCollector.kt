package org.billthefarmer.currency.composition.holder

import org.billthefarmer.currency.composition.core.Alias

class InstanceCollector<Type, Value> : MutableInstanceHolder<Type, Value> {

    private val instances = hashMapOf<TypedValue<Type>, Value>()

    override fun get(type: Type, alias: Alias?): Value? {
        val key = TypedValue(type, alias)
        return instances[key]
    }

    override fun getKeys(): Map<Type, List<Alias?>> {
        return instances.keys
            .groupBy { it.type }
            .mapValues { it.value.map { it.alias } }
    }

    override fun set(type: Type, alias: Alias?, value: Value) {
        val key = TypedValue(type, alias)
        instances[key] = value
    }

    data class TypedValue<T>(
        val type: T,
        val alias: Alias?
    )

}