package wiki.depasquale.currency.composition

import wiki.depasquale.composition.scope.CompositionScope
import wiki.depasquale.composition.scope.CompositionScopeDefault
import wiki.depasquale.composition.scope.factory
import wiki.depasquale.composition.scope.get
import wiki.depasquale.currency.presentation.adapter.CurrencyModelAdapter
import wiki.depasquale.currency.presentation.adapter.CurrencyModelAdapterImpl
import wiki.depasquale.currency.presentation.adapter.DaySnapshotAdapter
import wiki.depasquale.currency.presentation.adapter.DaySnapshotAdapterImpl
import wiki.depasquale.currency.presentation.view.ViewCompositionScaffold
import wiki.depasquale.currency.presentation.view.ViewCompositionSnackbar
import wiki.depasquale.currency.presentation.view.dashboard.*
import wiki.depasquale.currency.presentation.view.detail.*
import wiki.depasquale.currency.presentation.view.main.*
import wiki.depasquale.currency.presentation.view.selection.*

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
