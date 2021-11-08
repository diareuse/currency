package org.billthefarmer.currency.composition.instance

import org.billthefarmer.currency.composition.core.CompositionScope

typealias CreatorFactory<T> = CompositionScope.() -> T
