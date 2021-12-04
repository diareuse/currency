package org.billthefarmer.currency.presentation.view.main

import androidx.compose.runtime.Composable
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.screen.MainViewModel
import org.billthefarmer.currency.tooling.ComposeTest
import org.junit.Test

class MainViewCompositionNavControllerTest : ComposeTest() {

    private lateinit var viewModel: MainViewModel
    private lateinit var composition: MainViewCompositionNavController

    override fun prepare() {
        viewModel = MainViewModel()

    }

    @Test
    fun hasNavController() = inCompose {
        val source = object : MainViewComposition {
            @Composable
            override fun Compose(model: MainViewModel) {
                val controller = LocalNavHostController.current
                assertThat(controller).isNotNull()
                assertThat(controller.currentDestination).isNull()
            }
        }
        composition = MainViewCompositionNavController(source)
        composition.Compose(model = viewModel)
    } asserts {
    }

}