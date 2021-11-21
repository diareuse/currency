package org.billthefarmer.currency.domain.startup

import android.content.Context
import androidx.startup.Initializer
import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.currency.composition.Dependency

class StartupComposition : Initializer<CompositionScope> {

    override fun create(context: Context): CompositionScope {
        Dependency.start(context)
        return Dependency()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()

}