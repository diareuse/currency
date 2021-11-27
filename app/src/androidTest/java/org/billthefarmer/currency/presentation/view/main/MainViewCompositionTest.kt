package org.billthefarmer.currency.presentation.view.main

import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.composition.composed
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.MainViewModel
import org.junit.Test

class MainViewCompositionTest : ComposeTest() {

    private lateinit var view: MainViewComposition

    override fun prepare() {
        view = composed(Main)
    }

    @Test
    fun hasNavigation() = inCompose {
        view.Compose(model = MainViewModel())
    } asserts {
        onNodeWithTag("navigation").assertExists()
    }

}