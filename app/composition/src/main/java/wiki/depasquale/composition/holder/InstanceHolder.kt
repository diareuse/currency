package wiki.depasquale.composition.holder

import wiki.depasquale.composition.extra.Alias

interface InstanceHolder<Type, Value> {

    fun get(type: Type, alias: Alias? = null): Value?
    fun getKeys(): Map<Type, List<Alias?>>

}