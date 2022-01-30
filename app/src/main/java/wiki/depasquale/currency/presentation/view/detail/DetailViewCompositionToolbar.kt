package wiki.depasquale.currency.presentation.view.detail

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
import wiki.depasquale.currency.R
import wiki.depasquale.currency.presentation.view.main.LocalNavHostController
import wiki.depasquale.currency.screen.detail.DetailViewModel
import wiki.depasquale.currency.screen.style.shapes
import java.util.*

class DetailViewCompositionToolbar : DetailViewComposition {

    @Composable
    override fun Compose(model: DetailViewModel) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val controller = LocalNavHostController.current
            Button(modifier = Modifier.size(56.dp)) { controller.popBackStack() }
            Spacer(modifier = Modifier.size(32.dp))
            Toolbar(modifier = Modifier.weight(1f), model.currency)
        }
    }

    @Composable
    private fun Button(modifier: Modifier = Modifier, onClick: () -> Unit) {
        Surface(
            modifier = modifier
                .testTag("toolbar-action-button")
                .clickable(onClickLabel = "Go Back", role = Role.Button, onClick = onClick),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 16.dp
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
    private fun Toolbar(modifier: Modifier = Modifier, currency: Currency) {
        Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
            Text(
                modifier = Modifier
                    .testTag("toolbar-title"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                text = currency.displayName
            )
        }
    }

}