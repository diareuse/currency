package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.currency.presentation.view.ViewCompositionNoop
import org.billthefarmer.currency.presentation.view.dashboard.*
import org.billthefarmer.currency.presentation.view.detail.Detail
import org.billthefarmer.currency.presentation.view.detail.DetailViewComposition
import org.billthefarmer.currency.presentation.view.main.*

fun CompositionScopeDefault.Builder.presentationModule() = apply {
    factory(Main) { createMainViewComposition() }
    factory(Detail) { createDetailViewComposition() }
    factory(Dashboard) { createDashboardViewComposition() }
}

private fun CompositionScope.createDashboardViewComposition(): DashboardViewComposition {
    return DashboardViewCompositionScaffold(
        toolbar = DashboardViewCompositionToolbar(),
        content = DashboardViewCompositionContent(),
        input = DashboardViewCompositionCurrencyFork(
            onSelected = DashboardViewCompositionInput(),
            onMissing = DashboardViewCompositionInputHint(),
        )
    )
}

private fun CompositionScope.createDetailViewComposition(): DetailViewComposition {
    return ViewCompositionNoop()
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    result = MainViewCompositionNavController(result)
    result = MainViewCompositionTheme(result)
    return result
}
