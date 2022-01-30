package wiki.depasquale.composition.scope

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.composition.instance.Creator

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