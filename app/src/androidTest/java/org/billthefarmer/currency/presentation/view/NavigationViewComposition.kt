package org.billthefarmer.currency.presentation.view

import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.composition.composed
import org.billthefarmer.currency.presentation.model.MainViewModel
import org.billthefarmer.currency.presentation.view.main.Main
import org.billthefarmer.currency.tooling.ComposeTest
import org.junit.Test

class NavigationViewComposition : ComposeTest() {

    @Test
    fun hasNavigation() = inCompose {
        val composition: ViewComposition<MainViewModel> = composed(Main)
        composition.Compose(model = MainViewModel())
        onNodeWithTag("navigation").assertExists()
    }

}