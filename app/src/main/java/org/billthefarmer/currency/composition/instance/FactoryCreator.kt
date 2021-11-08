package org.billthefarmer.currency.composition.instance

import org.billthefarmer.currency.composition.core.CompositionScope

class FactoryCreator<T>(private val factory: CreatorFactory<T>) : Creator<T> {

    override fun getValue(scope: CompositionScope): T {
        return factory.invoke(scope)
    }

}