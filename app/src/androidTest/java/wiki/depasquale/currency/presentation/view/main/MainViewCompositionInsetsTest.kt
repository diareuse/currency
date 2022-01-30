package wiki.depasquale.currency.presentation.view.main

import com.google.accompanist.insets.LocalWindowInsets
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.currency.presentation.view.ViewComposition
import wiki.depasquale.currency.screen.MainViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class MainViewCompositionInsetsTest : ComposeTest() {

    private lateinit var composition: MainViewCompositionInsets
    private lateinit var viewModel: MainViewModel

    override fun prepare() {
        viewModel = MainViewModel()
    }

    @Test
    fun hasInsets() = inCompose {
        val source = ViewComposition<MainViewModel> {
            val insets = LocalWindowInsets.current
            assertThat(insets).isNotNull()
        }
        composition = MainViewCompositionInsets(source)
        composition.Compose(model = viewModel)
    } asserts {}

}