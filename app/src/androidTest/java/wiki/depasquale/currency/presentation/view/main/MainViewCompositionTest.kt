package wiki.depasquale.currency.presentation.view.main

import androidx.compose.ui.test.onNodeWithTag
import org.junit.Test
import wiki.depasquale.currency.composition.composed
import wiki.depasquale.currency.screen.MainViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class MainViewCompositionTest : ComposeTest() {

    private lateinit var view: MainViewComposition

    override fun prepare() {
        view = composed(Main)
    }

    @Test
    fun hasNavigation() = inCompose {
        view.Compose(model = MainViewModel())
    } asserts {
        onNodeWithTag("navigation").assertExists()
    }

}