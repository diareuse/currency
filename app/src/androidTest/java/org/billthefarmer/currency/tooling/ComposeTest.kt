package org.billthefarmer.currency.tooling

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.billthefarmer.currency.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ComposeTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Before
    abstract fun prepare()

    @After
    open fun tearDown() = Unit

    fun inCompose(before: () -> Unit = {}, body: @Composable () -> Unit): TestableExpression {
        before()
        compose.setContent(body)
        return TestableExpression()
    }

    infix fun TestableExpression.asserts(body: ComposeContentTestRule.() -> Unit) {
        body(compose)
    }

    class TestableExpression

}