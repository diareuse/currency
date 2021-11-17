package org.billthefarmer.currency.composition

import android.content.Context
import org.billthefarmer.composition.core.Alias
import org.billthefarmer.composition.core.buildCompositor
import org.billthefarmer.composition.core.factory
import org.billthefarmer.composition.core.get
import java.lang.ref.WeakReference

class Dependency private constructor(
    private val context: WeakReference<Context>
) {

    private val compositor = buildCompositor {
        factory { requireNotNull(context.get()) }
        domainModule()
        presentationModule()
        uiModule()
    }

    companion object {

        private lateinit var instance: Dependency

        @JvmName("getCompositor")
        operator fun invoke() = instance.compositor

        fun start(context: Context) {
            instance = Dependency(WeakReference(context))
        }

    }

}

inline fun <reified T : Any> composed(alias: Alias? = null): T {
    return Dependency().get(alias)
}

inline fun <reified T : Any> composedLazy(alias: Alias?): Lazy<T> {
    return lazy { composed(alias) }
}