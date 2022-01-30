package wiki.depasquale.composition.scope

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.composition.extra.Parameters
import wiki.depasquale.composition.holder.InstanceCollector
import wiki.depasquale.composition.holder.InstanceHolder
import wiki.depasquale.composition.holder.MutableInstanceHolder
import wiki.depasquale.composition.instance.Creator
import wiki.depasquale.composition.instance.CreatorFactory
import wiki.depasquale.composition.instance.CreatorSingle

class CompositionScopeDefault(
    private val registry: InstanceHolder<Class<*>, Creator<*>>
) : CompositionScope {

    override fun <T> get(type: Class<T>, alias: Alias?, params: Array<out Any?>): T {
        val creator = registry.get(type, alias)
        requireNotNull(creator) {
            "Creator for class=$type and alias=$alias is null. Did you register it?"
        }

        @Suppress("UNCHECKED_CAST")
        return creator.getValue(this, Parameters(params)) as T
    }

    // assume no side effects
    fun getKeys(): Map<Class<*>, List<Alias?>> {
        return registry.getKeys()
    }

    class Builder {

        private val registry: MutableInstanceHolder<Class<*>, Creator<*>> = InstanceCollector()

        fun <T : Any> single(type: Class<T>, alias: Alias?, creator: Creator.Factory<T>) = apply {
            registry.set(type, alias, CreatorSingle(creator))
        }

        fun <T : Any> factory(type: Class<T>, alias: Alias?, creator: Creator.Factory<T>) = apply {
            registry.set(type, alias, CreatorFactory(creator))
        }

        fun create(): CompositionScopeDefault {
            return CompositionScopeDefault(registry)
        }

    }

}