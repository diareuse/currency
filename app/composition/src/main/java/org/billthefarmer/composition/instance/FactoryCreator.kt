package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

class FactoryCreator<T>(private val factory: CreatorFactory<T>) : Creator<T> {

    override fun getValue(scope: CompositionScope): T {
        return factory.invoke(scope)
    }

}