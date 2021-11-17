package org.billthefarmer.composition.core

import org.billthefarmer.composition.holder.InstanceCollector
import org.billthefarmer.composition.holder.InstanceHolder
import org.billthefarmer.composition.holder.MutableInstanceHolder
import org.billthefarmer.composition.instance.Creator
import org.billthefarmer.composition.instance.CreatorFactory
import org.billthefarmer.composition.instance.FactoryCreator
import org.billthefarmer.composition.instance.SingleCreator

class Compositor(
    private val registry: InstanceHolder<Class<*>, Creator<*>>
) : CompositionScope {

    override fun <T> get(type: Class<T>, alias: Alias?): T {
        val creator = registry.get(type, alias)
        requireNotNull(creator) {
            "Creator for class=$type and alias=$alias is null. Did you register it?"
        }

        @Suppress("UNCHECKED_CAST")
        return creator.getValue(this) as T
    }

    // assume no side effects
    internal fun getKeys(): Map<Class<*>, List<Alias?>> {
        return registry.getKeys()
    }

    class Builder {

        private val registry: MutableInstanceHolder<Class<*>, Creator<*>> = InstanceCollector()

        fun <T : Any> single(type: Class<T>, alias: Alias?, creator: CreatorFactory<T>) = apply {
            registry.set(type, alias, SingleCreator(creator))
        }

        fun <T : Any> factory(type: Class<T>, alias: Alias?, creator: CreatorFactory<T>) = apply {
            registry.set(type, alias, FactoryCreator(creator))
        }

        fun create(): Compositor {
            return Compositor(registry)
        }

    }

}

inline fun buildCompositor(builder: Compositor.Builder.() -> Unit): Compositor {
    return Compositor.Builder().apply(builder).create()
}

inline fun <reified T : Any> Compositor.Builder.single(
    alias: Alias? = null,
    creator: CreatorFactory<T>
) = single(T::class.java, alias, creator)

inline fun <reified T : Any> Compositor.Builder.factory(
    alias: Alias? = null,
    creator: CreatorFactory<T>
) = factory(T::class.java, alias, creator)