package org.billthefarmer.composition.instance

import org.billthefarmer.composition.extra.Parameters
import org.billthefarmer.composition.scope.CompositionScope

class CreatorSingle<T : Any>(private val factory: Creator.Factory<T>) : Creator<T> {

    private val instances: HashMap<Int, T> = HashMap()

    override fun getValue(scope: CompositionScope, parameters: Parameters): T {
        val identifier = parameters.hashCode()
        return instances[identifier] ?: synchronized(this) {
            instances[identifier] ?: createInstance(scope, parameters).also {
                instances[identifier] = it
            }
        }
    }

    private fun createInstance(scope: CompositionScope, parameters: Parameters) =
        scope.run { factory.run { create(parameters) } }

}