package org.billthefarmer.currency.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

class ViewCompositionScaffold<Model>(
    private val toolbar: ViewComposition<Model>,
    private val content: ViewComposition<Model>
) : ViewComposition<Model> {

    @Composable
    override fun Compose(model: Model) {
        Surface(color = MaterialTheme.colors.surface) {
            ComposeLayout(model = model)
        }
    }

    @Composable
    private fun ComposeLayout(model: Model) {
        Column(modifier = Modifier.fillMaxSize()) {
            Toolbar(
                modifier = Modifier
                    .testTag("scaffold-toolbar"),
                model = model
            )
            Content(
                modifier = Modifier
                    .testTag("scaffold-content")
                    .weight(1f),
                model = model
            )
        }
    }

    @Composable
    private fun Toolbar(modifier: Modifier, model: Model) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(56.dp)
                .padding(32.dp)
                .statusBarsPadding()
        ) {
            toolbar.Compose(model = model)
        }
    }

    @Composable
    private fun Content(modifier: Modifier, model: Model) {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            content.Compose(model = model)
        }
    }

}