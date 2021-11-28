package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.selection.SelectionViewModel
import org.junit.Test

class SelectionViewCompositionScaffoldTest : ComposeTest() {

    private lateinit var viewModel: SelectionViewModel
    private lateinit var view: SelectionViewComposition

    override fun prepare() {
        viewModel = SelectionViewModel()
        view = SelectionViewCompositionScaffold(
            toolbar = ViewComposition { Box(modifier = Modifier.testTag("drawn-toolbar")) },
            content = ViewComposition { Box(modifier = Modifier.testTag("drawn-content")) }
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
    fun hasProvidedToolbar() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("drawn-toolbar").assertExists()
    }

    @Test
    fun hasContent() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("scaffold-content").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

    @Test
    fun hasProvidedContent() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("drawn-content").assertExists()
    }

}