package org.billthefarmer.currency.tooling

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

abstract class ComposeTest {

    @get:Rule
    val compose = createComposeRule()

    fun inCompose(body: @Composable ComposeContentTestRule.() -> Unit) {
        compose.setContent {
            body(compose)
        }
    }

}