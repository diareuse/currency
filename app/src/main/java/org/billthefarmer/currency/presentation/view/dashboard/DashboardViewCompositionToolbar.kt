package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.R
import org.billthefarmer.currency.ui.dashboard.DashboardViewModel

class DashboardViewCompositionToolbar : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        Compose()
    }

    @Composable
    private fun Compose() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Toolbar(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(32.dp))
            Button(modifier = Modifier.size(56.dp)) { TODO() }
        }
    }

    @Composable
    private fun Toolbar(modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.background
        ) {
            Text(
                modifier = Modifier
                    .testTag("toolbar-title")
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                style = MaterialTheme.typography.h6,
                text = "Welcome back!"
            )
        }
    }

    @Composable
    private fun Button(modifier: Modifier = Modifier, onClick: () -> Unit) {
        Surface(
            modifier = modifier
                .testTag("toolbar-action-button")
                .clickable(onClickLabel = "Add a currency", role = Role.Button, onClick = onClick),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.background
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentDescription = "add currency"
            )
        }
    }

    // ---

    @Preview
    @Composable
    private fun PreviewToolbar() {
        Compose()
    }

}