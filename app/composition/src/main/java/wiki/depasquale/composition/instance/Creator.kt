package wiki.depasquale.composition.instance

import wiki.depasquale.composition.extra.Parameters
import wiki.depasquale.composition.scope.CompositionScope

interface Creator<T : Any> {

    fun getValue(scope: CompositionScope, parameters: Parameters = Parameters.Empty): T

    fun interface Factory<T : Any> {
        fun CompositionScope.create(parameters: Parameters): T
    }

}