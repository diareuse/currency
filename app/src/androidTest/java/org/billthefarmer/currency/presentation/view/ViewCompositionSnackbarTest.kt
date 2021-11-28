package org.billthefarmer.currency.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import org.billthefarmer.currency.tooling.ComposeTest
import org.junit.Test

class ViewCompositionSnackbarTest : ComposeTest() {

    private lateinit var view: ViewCompositionSnackbar<Any>

    override fun prepare() {
        view = ViewCompositionSnackbar(
            ViewComposition { Box(modifier = Modifier.testTag("drawn-content")) },
        )
    }

    @Test
    fun drawsSource() = inCompose {
        view.Compose(model = Any())
    } asserts {
        onNodeWithTag("drawn-content").assertExists()
    }

    @Test
    fun providesControllerToChildren() = inCompose {
        view = ViewCompositionSnackbar(ViewComposition { LocalSnackbarController.current })
        view.Compose(model = Any())
    } asserts {
        // this will just crash if it's not provided
    }

    @Test
    fun isGone_whenNoSnackbarRequested() = inCompose {
        view.Compose(model = Any())
    } asserts {
        onNodeWithTag("snackbar-content").assertDoesNotExist()
        onNodeWithTag("snackbar-container").assertDoesNotExist()
    }

    @Test
    fun isVisible_whenSnackbarRequested() = inCompose {
        view = ViewCompositionSnackbar(ViewComposition { LocalSnackbarController.current.show("") })
        view.Compose(model = Any())
    } asserts {
        onNodeWithTag("snackbar-content").assertExists()
        onNodeWithTag("snackbar-container").assertExists()
    }

}