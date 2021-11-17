package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.ui.test.*
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel
import org.junit.Before
import org.junit.Test

class DashboardViewCompositionToolbarTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionToolbar

    @Before
    fun prepare() {
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionToolbar()
    }

    @Test
    fun hasTitle() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("toolbar-title").assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHeightIsAtLeast(25.dp)
            .assertHasNoClickAction()
    }

    @Test
    fun hasActionButton() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithContentDescription("add currency").assertExists()
        onNodeWithTag("toolbar-action-button").assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHeightIsAtLeast(48.dp)
            .assertHasClickAction()
    }

}