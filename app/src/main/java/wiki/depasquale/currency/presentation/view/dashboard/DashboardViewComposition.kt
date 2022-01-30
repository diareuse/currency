package wiki.depasquale.currency.presentation.view.dashboard

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.currency.presentation.view.ViewComposition
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel

val Dashboard = Alias("dashboard-view-composition")
typealias DashboardViewComposition = ViewComposition<DashboardViewModel>