package org.billthefarmer.currency.presentation.view.main

import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.composition.composed
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.MainViewModel
import org.junit.Test

class NavigationViewComposition : ComposeTest() {

    @Test
    fun hasNavigation() = inCompose {
        val composition: ViewComposition<MainViewModel> = composed(Main)
        composition.Compose(model = MainViewModel())
    } asserts {
        onNodeWithTag("navigation").assertExists()
    }

}