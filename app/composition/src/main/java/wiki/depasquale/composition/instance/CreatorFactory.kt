package wiki.depasquale.composition.instance

import wiki.depasquale.composition.extra.Parameters
import wiki.depasquale.composition.scope.CompositionScope

class CreatorFactory<T : Any>(private val factory: Creator.Factory<T>) : Creator<T> {

    override fun getValue(scope: CompositionScope, parameters: Parameters): T {
        return scope.run { factory.run { create(parameters) } }
    }

}