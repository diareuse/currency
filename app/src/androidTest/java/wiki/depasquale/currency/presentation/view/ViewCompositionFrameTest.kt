package wiki.depasquale.currency.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Test
import wiki.depasquale.currency.tooling.ComposeTest

class ViewCompositionFrameTest : ComposeTest() {

    private lateinit var view: ViewCompositionFrame<Any>

    override fun prepare() {
        view = ViewCompositionFrame(
            ViewComposition { Box(Modifier.testTag("1")) },
            ViewComposition { Box(Modifier.testTag("2")) },
            ViewComposition { Box(Modifier.testTag("3")) },
            ViewComposition { Box(Modifier.testTag("4")) },
        )
    }

    @Test
    fun laysOutProvidedViews() = inCompose {
        view.Compose(model = Any())
    } asserts {
        onNodeWithTag("1").assertExists()
        onNodeWithTag("2").assertExists()
        onNodeWithTag("3").assertExists()
        onNodeWithTag("4").assertExists()
    }

}