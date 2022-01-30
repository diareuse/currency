package wiki.depasquale.composition.holder

import wiki.depasquale.composition.extra.Alias

interface MutableInstanceHolder<Type, Value> : InstanceHolder<Type, Value> {

    fun set(type: Type, alias: Alias?, value: Value)

}