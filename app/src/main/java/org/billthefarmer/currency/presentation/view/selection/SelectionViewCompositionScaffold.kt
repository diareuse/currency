package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import org.billthefarmer.currency.ui.selection.SelectionViewModel

class SelectionViewCompositionScaffold(
    private val toolbar: SelectionViewComposition,
    private val content: SelectionViewComposition
) : SelectionViewComposition {

    @Composable
    override fun Compose(model: SelectionViewModel) {
        Surface(color = MaterialTheme.colors.surface) {
            ComposeLayout(model = model)
        }
    }

    @Composable
    private fun ComposeLayout(model: SelectionViewModel) {
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
    private fun Toolbar(modifier: Modifier, model: SelectionViewModel) {
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
    private fun Content(modifier: Modifier, model: SelectionViewModel) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            content.Compose(model = model)
        }
    }

}