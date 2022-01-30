package wiki.depasquale.currency.composition

import wiki.depasquale.composition.scope.CompositionScopeDefault
import wiki.depasquale.composition.scope.factory
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.screen.detail.DetailViewModel
import wiki.depasquale.currency.screen.selection.SelectionViewModel
import java.util.*

fun CompositionScopeDefault.Builder.uiModule() = apply {
    factory { DashboardViewModel() }
    factory { SelectionViewModel() }
    factory { (c: Currency) -> DetailViewModel(c) }
}