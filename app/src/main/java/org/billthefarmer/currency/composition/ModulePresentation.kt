package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.currency.presentation.view.main.Main
import org.billthefarmer.currency.presentation.view.main.MainViewComposition
import org.billthefarmer.currency.presentation.view.main.MainViewCompositionNavigation

fun CompositionScopeDefault.Builder.presentationModule() = apply {
    factory(Main) { createMainViewComposition() }
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    return result
}
