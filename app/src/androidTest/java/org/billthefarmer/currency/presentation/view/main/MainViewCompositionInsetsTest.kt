package org.billthefarmer.currency.presentation.view.main

import com.google.accompanist.insets.LocalWindowInsets
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.tooling.ComposeTest
import org.billthefarmer.currency.ui.MainViewModel
import org.junit.Test

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