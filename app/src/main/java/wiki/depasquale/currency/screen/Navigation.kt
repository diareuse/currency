package wiki.depasquale.currency.screen

import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import wiki.depasquale.currency.screen.favorite.FavoriteScreen
import wiki.depasquale.currency.screen.history.HistoryScreen
import wiki.depasquale.currency.screen.listing.ListingScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val controller = rememberAnimatedNavController()
    AnimatedNavHost(controller, "/favorites") {
        composableCard("/favorites") {
            FavoriteScreen(
                viewModel = hiltViewModel(),
                onNavigateToListing = {
                    controller.navigate("/listing")
                },
                onNavigateToHistory = {
                    controller.navigate("/favorites/history")
                }
            )
        }
        composableCard("/listing") {
            ListingScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = controller::navigateUp
            )
        }
        composableCard("/favorites/history") {
            HistoryScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = controller::navigateUp
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.composableCard(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit)
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = {
        slideIntoContainer(
            SlideDirection.Left,
            animationSpec = tween(durationMillis = 500)
        )
    },
    exitTransition = {
        slideOutOfContainer(
            SlideDirection.Left,
            animationSpec = tween(durationMillis = 500),
            targetOffset = { it / 2 }
        ) + fadeOut()
    },
    popEnterTransition = {
        slideIntoContainer(
            SlideDirection.Right,
            animationSpec = tween(durationMillis = 500),
            initialOffset = { it / 2 }
        ) + fadeIn()
    },
    popExitTransition = {
        slideOutOfContainer(
            SlideDirection.Right,
            animationSpec = tween(durationMillis = 500)
        )
    },
    content = content
)