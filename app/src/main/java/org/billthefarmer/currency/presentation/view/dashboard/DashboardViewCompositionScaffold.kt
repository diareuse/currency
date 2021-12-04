package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel

class DashboardViewCompositionScaffold(
    private val toolbar: DashboardViewComposition,
    private val content: DashboardViewComposition,
    private val input: DashboardViewComposition,
) : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        Surface(color = MaterialTheme.colors.background) {
            ComposeLayout(model = model)
        }
    }

    @Composable
    private fun ComposeLayout(model: DashboardViewModel) {
        Column {
            Content(modifier = Modifier.weight(1f), model)
            Input(model)
        }
    }

    @Composable
    private fun Content(modifier: Modifier, model: DashboardViewModel) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.large.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .testTag("scaffold-toolbar")
                        .defaultMinSize(minHeight = 56.dp)
                        .padding(32.dp)
                        .statusBarsPadding()
                ) {
                    toolbar.Compose(model = model)
                }
                Box(
                    modifier = Modifier
                        .testTag("scaffold-content")
                        .weight(1f)
                ) {
                    content.Compose(model = model)
                }
            }
        }
    }

    @Composable
    private fun Input(model: DashboardViewModel) {
        Box(
            modifier = Modifier
                .testTag("scaffold-input")
                .defaultMinSize(minHeight = 56.dp)
                .padding(32.dp)
                .navigationBarsWithImePadding()
        ) {
            input.Compose(model = model)
        }
    }

}