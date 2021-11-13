package org.billthefarmer.composition.instance

import org.billthefarmer.composition.core.CompositionScope

typealias CreatorFactory<T> = CompositionScope.() -> T
