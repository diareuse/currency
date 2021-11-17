package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

class FactoryCreator<T : Any>(private val factory: CreatorFactory<T>) : Creator<T> {

    override fun getValue(scope: CompositionScope): T {
        return scope.run { factory.run { create() } }
    }

}