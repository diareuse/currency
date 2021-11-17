package org.billthefarmer.composition.instance

import org.billthefarmer.composition.extra.Parameters
import org.billthefarmer.composition.scope.CompositionScope

class CreatorFactory<T : Any>(private val factory: Creator.Factory<T>) : Creator<T> {

    override fun getValue(scope: CompositionScope, parameters: Parameters): T {
        return scope.run { factory.run { create(parameters) } }
    }

}