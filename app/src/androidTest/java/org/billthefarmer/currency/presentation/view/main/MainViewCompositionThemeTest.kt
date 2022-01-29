package org.billthefarmer.currency.presentation.view.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.screen.MainViewModel
import org.billthefarmer.currency.tooling.ComposeTest
import org.junit.Test

class MainViewCompositionThemeTest : ComposeTest() {

    private lateinit var composition: MainViewCompositionTheme
    private lateinit var viewModel: MainViewModel

    override fun prepare() {
        viewModel = MainViewModel()
    }

    @Test
    fun hasColors() = inCompose {
        val source = ViewComposition<MainViewModel> {
            assertThat(MaterialTheme.colors.primary).isEqualTo(Color(0xFF_333042))
            assertThat(MaterialTheme.colors.primaryVariant).isEqualTo(Color(0xFF_d31e21))
            assertThat(MaterialTheme.colors.secondary).isEqualTo(Color(0xFF_f7a52d))
            assertThat(MaterialTheme.colors.secondaryVariant).isEqualTo(Color(0xFF_f7a52d))
            assertThat(MaterialTheme.colors.background).isEqualTo(Color(0xFF_f1f1f1))
            assertThat(MaterialTheme.colors.surface).isEqualTo(Color(0xFF_ffffff))
            assertThat(MaterialTheme.colors.error).isEqualTo(Color(0xFF_ff4000))
            assertThat(MaterialTheme.colors.onPrimary).isEqualTo(Color(0xFF_ffffff))
            assertThat(MaterialTheme.colors.onSecondary).isEqualTo(Color(0xFF_000000))
            assertThat(MaterialTheme.colors.onBackground).isEqualTo(Color(0xFF_000000))
            assertThat(MaterialTheme.colors.onSurface).isEqualTo(Color(0xFF_000000))
            assertThat(MaterialTheme.colors.onError).isEqualTo(Color(0xFF_ffffff))
        }
        composition = MainViewCompositionTheme(source)
        composition.Compose(model = viewModel)
    } asserts {}

    @Test
    fun hasShapes() = inCompose {
        val source = ViewComposition<MainViewModel> {
            assertThat(MaterialTheme.shapes.small).isEqualTo(RoundedCornerShape(8.dp))
            assertThat(MaterialTheme.shapes.medium).isEqualTo(RoundedCornerShape(12.dp))
            assertThat(MaterialTheme.shapes.large).isEqualTo(RoundedCornerShape(32.dp))
        }
        composition = MainViewCompositionTheme(source)
        composition.Compose(model = viewModel)
    } asserts {}

}