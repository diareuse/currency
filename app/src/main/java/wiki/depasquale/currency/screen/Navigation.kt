package wiki.depasquale.currency.screen

import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.createGraph
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import cursola.view.VerticalDivider
import wiki.depasquale.currency.screen.favorite.FavoriteScreen
import wiki.depasquale.currency.screen.history.HistoryScreen
import wiki.depasquale.currency.screen.listing.ListingScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(size: WindowSizeClass) {
    val controller = rememberAnimatedNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    controller.setLifecycleOwner(lifecycleOwner)
    controller.setViewModelStore(viewModelStoreOwner.viewModelStore)
    // todo fix transitions between screen sizes
    when (size.widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationMobile(controller)
        WindowWidthSizeClass.Medium -> NavigationMedium(controller)
        WindowWidthSizeClass.Expanded -> NavigationExpanded(controller)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationMobile(controller: NavHostController) {
    AnimatedNavHost(controller, "/favorites") {
        composableCard("/favorites") { FavoriteScreen(controller) }
        composableCard("/favorites/history") { HistoryScreen(controller) }
        composableCard("/listing") { ListingScreen(controller) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationMedium(controller: NavHostController) {
    val startDestination = "/favorites/history"
    val graph = remember(startDestination) {
        controller.createGraph(startDestination, null) {
            composableCard("/favorites/history") { HistoryScreen(controller) }
            composableCard("/listing") { ListingScreen(controller) }
        }.also {
            controller.graph = it
        }
    }
    Row {
        FavoriteScreen(modifier = Modifier.weight(1f), controller = controller)
        VerticalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding()
        )
        AnimatedNavHost(
            modifier = Modifier.weight(1f),
            graph = graph,
            navController = controller
        )
    }
}

@Composable
fun NavigationExpanded(controller: NavHostController) {
    remember {
        controller.createGraph("/favorites/history", null) {
            composableCard("/favorites/history") {}
        }.also {
            controller.graph = it
        }
    }
    Row(modifier = Modifier.fillMaxSize()) {
        FavoriteScreen(modifier = Modifier.weight(1f), controller = controller)
        VerticalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding()
        )
        HistoryScreen(modifier = Modifier.weight(1f), controller = controller)
        VerticalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding()
        )
        ListingScreen(modifier = Modifier.weight(1f), controller = controller)
    }
}

@Composable
private fun FavoriteScreen(
    controller: NavHostController,
    modifier: Modifier = Modifier,
) {
    FavoriteScreen(
        modifier = modifier,
        viewModel = hiltViewModel(),
        onNavigateToListing = navigateTask(controller, "/listing"),
        onNavigateToHistory = navigateTask(controller, "/favorites/history")
    )
}

@Composable
private fun ListingScreen(
    controller: NavHostController,
    modifier: Modifier = Modifier,
) {
    ListingScreen(
        modifier = modifier,
        viewModel = hiltViewModel(),
        onNavigateBack = navigateUpTask(controller, "/listing")
    )
}

@Composable
private fun HistoryScreen(
    controller: NavHostController,
    modifier: Modifier = Modifier,
) {
    HistoryScreen(
        modifier = modifier,
        viewModel = hiltViewModel(),
        onNavigateBack = navigateUpTask(controller, "/favorites/history")
    )
}

private fun navigateTask(
    controller: NavHostController,
    route: String
): (() -> Unit)? {
    if (controller.graph.none { it.route == route }) return null
    if (controller.currentDestination?.route == route) return null
    return { controller.navigate(route) }
}

private fun navigateUpTask(controller: NavHostController, fromRoute: String): (() -> Unit)? {
    if (controller.graph.none { it.route == fromRoute }) return null
    if (
        controller.backQueue.asSequence()
            .map { it.destination.route }
            .filterNot { it.isNullOrBlank() || it == fromRoute }
            .count() == 0
    ) return null
    return { controller.navigateUp() }
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
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    },
    exitTransition = {
        slideOutOfContainer(
            SlideDirection.Left,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing),
            targetOffset = { it / 2 }
        ) + fadeOut()
    },
    popEnterTransition = {
        slideIntoContainer(
            SlideDirection.Right,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing),
            initialOffset = { it / 2 }
        ) + fadeIn()
    },
    popExitTransition = {
        slideOutOfContainer(
            SlideDirection.Right,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    },
    content = content
)