package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

class SingleCreator<T : Any>(private val factory: CreatorFactory<T>) : Creator<T> {

    private var instance: T? = null

    override fun getValue(scope: CompositionScope): T {
        return instance ?: synchronized(this) {
            instance ?: return createInstance(scope).also { newInstance ->
                instance = newInstance
            }
        }
    }

    private fun createInstance(scope: CompositionScope) =
        scope.run { factory.run { create() } }

}