package org.billthefarmer.composition.scope

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.composition.instance.Creator

inline fun buildComposition(builder: CompositionScopeDefault.Builder.() -> Unit): CompositionScopeDefault {
    return CompositionScopeDefault.Builder().apply(builder).create()
}

inline fun <reified T : Any> CompositionScopeDefault.Builder.single(
    alias: Alias? = null,
    creator: Creator.Factory<T>
) = single(T::class.java, alias, creator)

inline fun <reified T : Any> CompositionScopeDefault.Builder.factory(
    alias: Alias? = null,
    creator: Creator.Factory<T>
) = factory(T::class.java, alias, creator)