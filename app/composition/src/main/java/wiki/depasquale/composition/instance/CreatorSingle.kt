package wiki.depasquale.composition.instance

import wiki.depasquale.composition.extra.Parameters
import wiki.depasquale.composition.scope.CompositionScope

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