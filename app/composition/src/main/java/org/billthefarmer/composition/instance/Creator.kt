package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

interface Creator<T> {

    fun getValue(scope: CompositionScope): T

}