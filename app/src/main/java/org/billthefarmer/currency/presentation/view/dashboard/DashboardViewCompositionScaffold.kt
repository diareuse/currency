package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel

class DashboardViewCompositionScaffold(
    private val toolbar: DashboardViewComposition,
    private val content: DashboardViewComposition,
    private val input: DashboardViewComposition,
) : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        Column {
            Box(
                modifier = Modifier
                    .testTag("scaffold-toolbar")
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(32.dp)
            ) {
                toolbar.Compose(model = model)
            }
            Box(
                modifier = Modifier
                    .testTag("scaffold-content")
                    .weight(1f)
                    .padding(32.dp)
            ) {
                content.Compose(model = model)
            }
            Box(
                modifier = Modifier
                    .testTag("scaffold-input")
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(32.dp)
            ) {
                input.Compose(model = model)
            }
        }
    }

}