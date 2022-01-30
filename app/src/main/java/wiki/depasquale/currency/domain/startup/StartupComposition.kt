package wiki.depasquale.currency.domain.startup

import android.content.Context
import androidx.startup.Initializer
import wiki.depasquale.composition.scope.CompositionScope
import wiki.depasquale.currency.composition.Dependency

class StartupComposition : Initializer<CompositionScope> {

    override fun create(context: Context): CompositionScope {
        Dependency.start(context)
        return Dependency()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()

}