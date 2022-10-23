package wiki.depasquale.currency.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import wiki.depasquale.currency.screen.favorite.FavoriteScreen

@Composable
fun Navigation() {
    val controller = rememberNavController()
    NavHost(controller, "/favorites") {
        composable("/favorites") {
            FavoriteScreen(
                viewModel = hiltViewModel(),
                onNavigateToListing = {
                    controller.navigate("/listing")
                }
            )
        }
        composable("/listing") {
            Text("👋")
        }
    }
}