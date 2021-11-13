package org.billthefarmer.currency.presentation.view.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.billthefarmer.currency.presentation.model.MainViewModel

class MainViewCompositionNavigation : MainViewComposition {

    @Composable
    override fun Compose(model: MainViewModel) {
        val controller = rememberNavController()
        NavHost(
            modifier = Modifier.testTag("navigation"),
            navController = controller,
            startDestination = "start"
        ) {
            composable("start") {}
        }
    }

}