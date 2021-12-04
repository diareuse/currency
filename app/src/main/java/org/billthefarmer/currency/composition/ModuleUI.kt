package org.billthefarmer.currency.composition

import org.billthefarmer.composition.scope.CompositionScopeDefault
import org.billthefarmer.composition.scope.factory
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel
import org.billthefarmer.currency.screen.detail.DetailViewModel
import org.billthefarmer.currency.screen.selection.SelectionViewModel
import java.util.*

fun CompositionScopeDefault.Builder.uiModule() = apply {
    factory { DashboardViewModel() }
    factory { SelectionViewModel() }
    factory { (c: Currency) -> DetailViewModel(c) }
}