package wiki.depasquale.currency.composition

import android.content.Context
import wiki.depasquale.composition.scope.buildComposition
import wiki.depasquale.composition.scope.factory
import java.lang.ref.WeakReference

class Dependency private constructor(
    private val context: WeakReference<Context>
) {

    private val compositor = buildComposition {
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
