package wiki.depasquale.currency.presentation.view.selection

import androidx.compose.ui.test.*
import androidx.compose.ui.unit.dp
import org.junit.Test
import wiki.depasquale.currency.screen.selection.SelectionViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class SelectionViewCompositionToolbarTest : ComposeTest() {

    private lateinit var viewModel: SelectionViewModel
    private lateinit var view: SelectionViewComposition

    override fun prepare() {
        viewModel = SelectionViewModel()
        view = SelectionViewCompositionToolbar()
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
        onNodeWithContentDescription("go back").assertExists()
        onNodeWithTag("toolbar-action-button").assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHeightIsAtLeast(48.dp)
            .assertHasClickAction()
    }

}