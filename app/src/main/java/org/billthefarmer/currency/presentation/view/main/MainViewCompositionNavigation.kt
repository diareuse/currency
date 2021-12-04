package org.billthefarmer.currency.presentation.view.main

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.billthefarmer.currency.composition.composedViewModel
import org.billthefarmer.currency.composition.rememberComposed
import org.billthefarmer.currency.presentation.view.dashboard.Dashboard
import org.billthefarmer.currency.presentation.view.dashboard.DashboardViewComposition
import org.billthefarmer.currency.presentation.view.detail.Detail
import org.billthefarmer.currency.presentation.view.detail.DetailViewComposition
import org.billthefarmer.currency.presentation.view.selection.Selection
import org.billthefarmer.currency.presentation.view.selection.SelectionViewComposition
import org.billthefarmer.currency.screen.MainViewModel

class MainViewCompositionNavigation : MainViewComposition {

    private val argCurrency = navArgument("currency") {
        type = NavType.StringType
        nullable = false
    }

    @Composable
    override fun Compose(model: MainViewModel) {
        NavHost(
            modifier = Modifier.testTag("navigation"),
            navController = LocalNavHostController.current,
            startDestination = "dashboard"
        ) {
            composable("dashboard") { Dashboard(it) }
            composable("detail/{currency}", listOf(argCurrency)) { Detail(it) }
            composable("selection") { Selection(it) }
        }
    }

    @Composable
    private fun Dashboard(entry: NavBackStackEntry) {
        val view = rememberComposed<DashboardViewComposition>(Dashboard)
        view.Compose(composedViewModel(screen = entry.destination.navigatorName))
    }

    @Composable
    private fun Detail(entry: NavBackStackEntry) {
        val arguments = entry.arguments ?: Bundle.EMPTY
        val view = rememberComposed<DetailViewComposition>(Detail, arguments)
        view.Compose(composedViewModel(screen = entry.destination.navigatorName))
    }

    @Composable
    private fun Selection(entry: NavBackStackEntry) {
        val view = rememberComposed<SelectionViewComposition>(Selection)
        view.Compose(composedViewModel(screen = entry.destination.navigatorName))
    }

}