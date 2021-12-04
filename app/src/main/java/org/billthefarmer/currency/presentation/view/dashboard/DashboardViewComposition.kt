package org.billthefarmer.currency.presentation.view.dashboard

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel

val Dashboard = Alias("dashboard-view-composition")
typealias DashboardViewComposition = ViewComposition<DashboardViewModel>