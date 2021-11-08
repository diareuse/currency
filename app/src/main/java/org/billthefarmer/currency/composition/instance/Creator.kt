package org.billthefarmer.currency.composition.instance

import org.billthefarmer.currency.composition.core.CompositionScope

interface Creator<T> {

    fun getValue(scope: CompositionScope): T

}