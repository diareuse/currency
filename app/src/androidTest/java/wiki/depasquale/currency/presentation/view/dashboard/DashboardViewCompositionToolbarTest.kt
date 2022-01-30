package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.ui.test.*
import androidx.compose.ui.unit.dp
import org.junit.Test
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class DashboardViewCompositionToolbarTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionToolbar

    override fun prepare() {
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