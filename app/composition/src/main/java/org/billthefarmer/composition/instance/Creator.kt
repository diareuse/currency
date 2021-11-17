package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

interface Creator<T : Any> {

    fun getValue(scope: CompositionScope, parameters: Parameters = Parameters.Empty): T

}