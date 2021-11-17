package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.currency.presentation.view.ViewCompositionNoop
import org.billthefarmer.currency.presentation.view.dashboard.Dashboard
import org.billthefarmer.currency.presentation.view.dashboard.DashboardViewComposition
import org.billthefarmer.currency.presentation.view.detail.Detail
import org.billthefarmer.currency.presentation.view.detail.DetailViewComposition
import org.billthefarmer.currency.presentation.view.main.Main
import org.billthefarmer.currency.presentation.view.main.MainViewComposition
import org.billthefarmer.currency.presentation.view.main.MainViewCompositionNavController
import org.billthefarmer.currency.presentation.view.main.MainViewCompositionNavigation

fun CompositionScopeDefault.Builder.presentationModule() = apply {
    factory(Main) { createMainViewComposition() }
    factory(Detail) { createDetailViewComposition() }
    factory(Dashboard) { createDashboardViewComposition() }
}

private fun CompositionScope.createDashboardViewComposition(): DashboardViewComposition {
    return ViewCompositionNoop()
}

private fun CompositionScope.createDetailViewComposition(): DetailViewComposition {
    return ViewCompositionNoop()
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    result = MainViewCompositionNavController(result)
    return result
}
