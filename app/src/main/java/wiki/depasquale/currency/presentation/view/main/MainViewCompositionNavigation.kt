package wiki.depasquale.currency.presentation.view.main

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import wiki.depasquale.currency.composition.composedViewModel
import wiki.depasquale.currency.composition.rememberComposed
import wiki.depasquale.currency.presentation.view.dashboard.Dashboard
import wiki.depasquale.currency.presentation.view.dashboard.DashboardViewComposition
import wiki.depasquale.currency.presentation.view.detail.Detail
import wiki.depasquale.currency.presentation.view.detail.DetailViewComposition
import wiki.depasquale.currency.presentation.view.selection.Selection
import wiki.depasquale.currency.presentation.view.selection.SelectionViewComposition
import wiki.depasquale.currency.screen.MainViewModel
import java.util.*

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
        val currency = Currency.getInstance(arguments.getString("currency"))
        val view = rememberComposed<DetailViewComposition>(Detail)
        view.Compose(
            composedViewModel(
                screen = entry.destination.navigatorName,
                params = arrayOf(currency)
            )
        )
    }

    @Composable
    private fun Selection(entry: NavBackStackEntry) {
        val view = rememberComposed<SelectionViewComposition>(Selection)
        view.Compose(composedViewModel(screen = entry.destination.navigatorName))
    }

}