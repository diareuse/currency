package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

fun interface CreatorFactory<T : Any> {
    fun CompositionScope.create(): T
}
