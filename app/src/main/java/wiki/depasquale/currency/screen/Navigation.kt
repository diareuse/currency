@file:Suppress("StringLiteralDuplication")

package wiki.depasquale.currency.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cursola.rate.view.FavoritesViewModel
import cursola.rate.view.HistoricalViewModel
import cursola.rate.view.ListingViewModel
import cursola.view.VerticalDivider
import wiki.depasquale.currency.screen.favorite.FavoriteScreen
import wiki.depasquale.currency.screen.history.HistoryScreen
import wiki.depasquale.currency.screen.listing.ListingScreen

@Composable
fun Navigation(size: WindowSizeClass) {
    when (size.widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationMobile()
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> NavigationExpanded()
    }
}

@Composable
fun NavigationMobile(
    controller: NavHostController = rememberNavController()
) {
    NavHost(
        navController = controller,
        startDestination = "/favorites"
    ) {
        composable("/favorites") {
            FavoriteScreen(
                onNavigateToHistory = { controller.navigate("/favorites/history") },
                onNavigateToListing = { controller.navigate("/listing") },
            )
        }
        composable("/favorites/history") { HistoryScreen(onNavigateBack = controller::navigateUp) }
        composable("/listing") { ListingScreen(onNavigateBack = controller::navigateUp) }
    }
}

@Composable
fun NavigationExpanded(
    mainController: NavHostController = rememberNavController(),
    detailController: NavHostController = rememberNavController()
) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = mainController,
            startDestination = "/favorites"
        ) {
            composable("/favorites") {
                FavoriteScreen(
                    onNavigateToHistory = { detailController.navigate("/favorites/history") },
                    onNavigateToListing = { detailController.navigate("/listing") },
                )
            }
        }
        VerticalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding()
        )
        NavHost(
            modifier = Modifier.weight(1f),
            navController = detailController,
            startDestination = "/none"
        ) {
            composable("/none") {}
            composable("/favorites/history") { HistoryScreen(onNavigateBack = detailController::navigateUp) }
            composable("/listing") { ListingScreen(onNavigateBack = detailController::navigateUp) }
        }
    }
}

@Composable
private fun FavoriteScreen(
    onNavigateToListing: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<FavoritesViewModel>()
    val items by viewModel.items.collectAsState(initial = emptyList())
    val selected by viewModel.selected.collectAsState()
    val value by viewModel.value.collectAsState()
    FavoriteScreen(
        modifier = modifier,
        items = items,
        selected = selected,
        value = value,
        onValueChanged = { viewModel.value.value = it },
        onItemSelected = { viewModel.selected.value = it.currency },
        onNavigateToListing = onNavigateToListing,
        onNavigateToHistory = onNavigateToHistory,
    )
}

@Composable
private fun ListingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<ListingViewModel>()
    val items by viewModel.items.collectAsState(initial = emptyList())
    ListingScreen(
        modifier = modifier,
        items = items,
        onAddItem = viewModel::add,
        onRemoveItem = viewModel::remove,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun HistoryScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<HistoricalViewModel>()
    val items by viewModel.items.collectAsState()
    HistoryScreen(
        items = items,
        modifier = modifier,
        onNavigateBack = onNavigateBack
    )
}