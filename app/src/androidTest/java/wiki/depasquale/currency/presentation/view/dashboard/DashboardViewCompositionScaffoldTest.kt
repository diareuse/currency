package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Test
import wiki.depasquale.currency.presentation.view.ViewCompositionNoop
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class DashboardViewCompositionScaffoldTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionScaffold

    override fun prepare() {
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionScaffold(
            ViewCompositionNoop(),
            ViewCompositionNoop(),
            ViewCompositionNoop()
        )
    }

    @Test
    fun hasToolbar() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("scaffold-toolbar").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun hasContent() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("scaffold-content").assertExists()
            .assertHasNoClickAction()
    }

    @Test
    fun hasInput() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("scaffold-input").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

}