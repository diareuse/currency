package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.composition.scope.get
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapter
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapterImpl
import org.billthefarmer.currency.presentation.view.ViewCompositionNoop
import org.billthefarmer.currency.presentation.view.dashboard.*
import org.billthefarmer.currency.presentation.view.detail.Detail
import org.billthefarmer.currency.presentation.view.detail.DetailViewComposition
import org.billthefarmer.currency.presentation.view.main.*
import org.billthefarmer.currency.presentation.view.selection.*

fun CompositionScopeDefault.Builder.presentationModule() = apply {
    factory<CurrencyModelAdapter> { CurrencyModelAdapterImpl() }

    factory(Main) { createMainViewComposition() }
    factory(Detail) { createDetailViewComposition() }
    factory(Dashboard) { createDashboardViewComposition() }
    factory(Selection) { createSelectionViewComposition() }
}

private fun CompositionScope.createSelectionViewComposition(): SelectionViewComposition {
    var result: SelectionViewComposition
    result = SelectionViewCompositionScaffold(
        toolbar = SelectionViewCompositionToolbar(),
        content = SelectionViewCompositionContent()
    )
    result = SelectionViewCompositionContentLoader(result, get(RatesNotSelected), get())
    result = SelectionViewCompositionPendingConsumer(
        result,
        get(ExchangeRateModel),
        get(ExchangeRateModel)
    )
    return result
}

private fun CompositionScope.createDashboardViewComposition(): DashboardViewComposition {
    var result: DashboardViewComposition = DashboardViewCompositionScaffold(
        toolbar = DashboardViewCompositionToolbar(),
        content = DashboardViewCompositionContent(),
        input = DashboardViewCompositionCurrencyFork(
            onSelected = DashboardViewCompositionInput(),
            onMissing = DashboardViewCompositionInputHint(),
        )
    )
    result = DashboardViewCompositionContentLoader(result, get(RatesToday), get())
    return result
}

private fun CompositionScope.createDetailViewComposition(): DetailViewComposition {
    return ViewCompositionNoop()
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    result = MainViewCompositionNavController(result)
    result = MainViewCompositionTheme(result)
    result = MainViewCompositionInsets(result)
    return result
}
