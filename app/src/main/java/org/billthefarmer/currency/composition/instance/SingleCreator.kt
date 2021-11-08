package org.billthefarmer.currency.composition.instance

import org.billthefarmer.currency.composition.core.CompositionScope

class SingleCreator<T>(private val factory: CreatorFactory<T>) : Creator<T> {

    private var instance: T? = null

    override fun getValue(scope: CompositionScope): T {
        return instance ?: synchronized(this) {
            instance ?: factory.invoke(scope).also { newInstance ->
                instance = newInstance
            }
        }
    }

}