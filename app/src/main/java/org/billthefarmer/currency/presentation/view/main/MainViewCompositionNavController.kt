package org.billthefarmer.currency.presentation.view.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.billthefarmer.currency.ui.MainViewModel

class MainViewCompositionNavController(
    private val composition: MainViewComposition
) : MainViewComposition {

    @Composable
    override fun Compose(model: MainViewModel) {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavHostController provides navController) {
            composition.Compose(model = model)
        }
    }

}

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    throw IllegalStateException("NavController was not provided")
}
