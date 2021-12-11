package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScope
import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.composition.scope.get
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapter
import org.billthefarmer.currency.presentation.adapter.CurrencyModelAdapterImpl
import org.billthefarmer.currency.presentation.adapter.DaySnapshotAdapter
import org.billthefarmer.currency.presentation.adapter.DaySnapshotAdapterImpl
import org.billthefarmer.currency.presentation.view.ViewCompositionScaffold
import org.billthefarmer.currency.presentation.view.ViewCompositionSnackbar
import org.billthefarmer.currency.presentation.view.dashboard.*
import org.billthefarmer.currency.presentation.view.detail.*
import org.billthefarmer.currency.presentation.view.main.*
import org.billthefarmer.currency.presentation.view.selection.*

fun CompositionScopeDefault.Builder.presentationModule() = apply {
    factory<CurrencyModelAdapter> { CurrencyModelAdapterImpl() }
    factory<DaySnapshotAdapter> { DaySnapshotAdapterImpl() }

    factory(Main) { createMainViewComposition() }
    factory(Detail) { createDetailViewComposition() }
    factory(Dashboard) { createDashboardViewComposition() }
    factory(Selection) { createSelectionViewComposition() }
}

private fun CompositionScope.createSelectionViewComposition(): SelectionViewComposition {
    var result: SelectionViewComposition
    result = ViewCompositionScaffold(
        toolbar = SelectionViewCompositionToolbar(),
        content = SelectionViewCompositionContent()
    )
    result = SelectionViewCompositionContentLoader(result, get(RatesNotSelected), get())
    result = SelectionViewCompositionPendingConsumer(
        result,
        get(ExchangeRateModel),
        get(ExchangeRateModel)
    )
    result = ViewCompositionSnackbar(result)
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
    result = DashboardViewCompositionDeletionConsumer(
        result,
        get(ExchangeRateModel),
        get(ExchangeRateModel)
    )
    result = ViewCompositionSnackbar(result)
    return result
}

private fun CompositionScope.createDetailViewComposition(): DetailViewComposition {
    var result: DetailViewComposition
    result = ViewCompositionScaffold(
        toolbar = DetailViewCompositionToolbar(),
        content = DetailViewCompositionContent()
    )
    result = DetailViewCompositionContentLoader(result, get(Rates90Days), get())
    result = ViewCompositionSnackbar(result)
    return result
}

private fun CompositionScope.createMainViewComposition(): MainViewComposition {
    var result: MainViewComposition = MainViewCompositionNavigation()
    result = MainViewCompositionNavController(result)
    result = MainViewCompositionTheme(result)
    result = MainViewCompositionInsets(result)
    return result
}
