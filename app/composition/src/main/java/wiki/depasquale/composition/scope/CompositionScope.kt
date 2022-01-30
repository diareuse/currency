package wiki.depasquale.composition.scope

import wiki.depasquale.composition.extra.Alias

interface CompositionScope {

    fun <T> get(type: Class<T>, alias: Alias? = null, params: Array<out Any?> = emptyArray()): T

}