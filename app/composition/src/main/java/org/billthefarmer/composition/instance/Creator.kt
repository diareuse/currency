package org.billthefarmer.composition.instance

import org.billthefarmer.composition.extra.Parameters
import org.billthefarmer.composition.scope.CompositionScope

interface Creator<T : Any> {

    fun getValue(scope: CompositionScope, parameters: Parameters = Parameters.Empty): T

    fun interface Factory<T : Any> {
        fun CompositionScope.create(parameters: Parameters): T
    }

}