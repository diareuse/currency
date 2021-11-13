package org.billthefarmer.currency.composition

import org.billthefarmer.composition.core.CompositionScope
import org.billthefarmer.composition.core.Compositor
import org.billthefarmer.composition.core.factory
import org.billthefarmer.currency.presentation.view.main.Main
import org.billthefarmer.currency.presentation.view.main.MainViewComposition
import org.billthefarmer.currency.presentation.view.main.MainViewCompositionNavigation

fun Compositor.Builder.presentationModule() = apply {
    factory(Main) { createMainViewComposition() }
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    return result
}
