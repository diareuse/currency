package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.billthefarmer.currency.R
import org.billthefarmer.currency.presentation.view.dashboard.DashboardViewCompositionToolbar
import org.billthefarmer.currency.presentation.view.main.LocalNavHostController
import org.billthefarmer.currency.screen.selection.SelectionViewModel
import org.billthefarmer.currency.tooling.Duplicates

@Duplicates(DashboardViewCompositionToolbar::class)
class SelectionViewCompositionToolbar : SelectionViewComposition {

    @Composable
    override fun Compose(model: SelectionViewModel) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val controller = LocalNavHostController.current
            Button(modifier = Modifier.size(56.dp)) { controller.popBackStack() }
            Spacer(modifier = Modifier.size(32.dp))
            Toolbar(modifier = Modifier.weight(1f))
        }
    }

    @Composable
    private fun Button(modifier: Modifier = Modifier, onClick: () -> Unit) {
        Surface(
            modifier = modifier
                .testTag("toolbar-action-button")
                .clickable(onClickLabel = "Go Back", role = Role.Button, onClick = onClick),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.primary,
            elevation = 16.dp
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_back),
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentDescription = "go back"
            )
        }
    }

    @Composable
    private fun Toolbar(modifier: Modifier = Modifier) {
        Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
            Text(
                modifier = Modifier
                    .testTag("toolbar-title"),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Black,
                text = "Select a currency"
            )
        }
    }

}