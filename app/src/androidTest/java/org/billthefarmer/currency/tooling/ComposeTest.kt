package org.billthefarmer.currency.tooling

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.billthefarmer.currency.ui.MainActivity
import org.junit.Rule

abstract class ComposeTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    fun inCompose(body: @Composable () -> Unit): TestableExpression {
        compose.setContent(body)
        return TestableExpression()
    }

    infix fun TestableExpression.asserts(body: ComposeContentTestRule.() -> Unit) {
        body(compose)
    }

    class TestableExpression

}