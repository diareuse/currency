package wiki.depasquale.currency.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import wiki.depasquale.currency.screen.favorite.FavoriteScreen
import wiki.depasquale.currency.screen.listing.ListingScreen

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
            ListingScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = controller::navigateUp
            )
        }
    }
}