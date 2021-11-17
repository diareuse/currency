package org.billthefarmer.composition.instance

import org.billthefarmer.composition.extra.Parameters
import org.billthefarmer.composition.scope.CompositionScope

class CreatorSingle<T : Any>(private val factory: Creator.Factory<T>) : Creator<T> {

    private val instances: HashMap<Parameters, T> = HashMap()

    override fun getValue(scope: CompositionScope, parameters: Parameters): T {
        return instances[parameters] ?: synchronized(this) {
            instances[parameters] ?: createInstance(scope, parameters).also {
                instances[parameters] = it
            }
        }
    }

    private fun createInstance(scope: CompositionScope, parameters: Parameters) =
        scope.run { factory.run { create(parameters) } }

}