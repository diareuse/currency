package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.billthefarmer.currency.R
import org.billthefarmer.currency.presentation.view.main.LocalNavHostController
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel
import org.billthefarmer.currency.screen.style.AnticipateOvershootEasing
import org.billthefarmer.currency.screen.style.shapes

class DashboardViewCompositionToolbar : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        Compose()
    }

    @Composable
    private fun Compose() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val controller = LocalNavHostController.current
            Toolbar(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(32.dp))
            Button(modifier = Modifier.size(56.dp)) {
                controller.navigate("selection")
            }
        }
    }

    @Composable
    private fun Toolbar(modifier: Modifier = Modifier) {
        var isWelcomeMessage by remember { mutableStateOf(true) }
        Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
            AnimatedVisibility(
                visible = isWelcomeMessage,
                enter = slideInVertically(tween(easing = AnticipateOvershootEasing)) { it } + fadeIn(),
                exit = slideOutVertically(tween(easing = AnticipateOvershootEasing)) { -it } + fadeOut()
            ) {
                Text(
                    modifier = Modifier
                        .testTag("toolbar-title"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    text = "Welcome back!"
                )
            }
            AnimatedVisibility(
                visible = !isWelcomeMessage,
                enter = slideInVertically(tween(easing = AnticipateOvershootEasing)) { it } + fadeIn(),
                exit = slideOutVertically(tween(easing = AnticipateOvershootEasing)) { -it } + fadeOut()
            ) {
                Text(
                    modifier = Modifier
                        .testTag("toolbar-title"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    text = "Add new currency"
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(5000)
            isWelcomeMessage = false
        }
    }

    @Composable
    private fun Button(modifier: Modifier = Modifier, onClick: () -> Unit) {
        Surface(
            modifier = modifier
                .testTag("toolbar-action-button")
                .clickable(onClickLabel = "Add a currency", role = Role.Button, onClick = onClick),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 16.dp
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