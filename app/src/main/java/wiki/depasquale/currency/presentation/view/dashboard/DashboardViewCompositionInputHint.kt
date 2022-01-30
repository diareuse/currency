package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel

class DashboardViewCompositionInputHint : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        Compose()
    }

    @Composable
    private fun Compose() {
        Row(
            modifier = Modifier.testTag("input-hint"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = LocalContentColor.current
            Image(
                modifier = Modifier
                    .height(56.dp)
                    .width(112.dp),
                strokeWidth = 4.dp,
                color = color,
                cornerRadius = 8.dp
            )
            Spacer(modifier = Modifier.size(32.dp))
            Hint(
                modifier = Modifier.weight(1f),
            )
        }
    }

    @Composable
    private fun Image(modifier: Modifier, strokeWidth: Dp, color: Color, cornerRadius: Dp) {
        Box(modifier = modifier) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val effect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 5.dp.toPx()), 0f)
                val stroke = Stroke(strokeWidth.toPx(), pathEffect = effect)
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                    style = stroke
                )
            }
        }
    }

    @Composable
    private fun Hint(
        modifier: Modifier
    ) {
        Text(
            modifier = modifier,
            text = "Select a currency from the list above",
            style = MaterialTheme.typography.titleMedium
        )
    }

    // ---

    @Preview
    @Composable
    private fun Preview() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(32.dp)
        ) {
            Compose()
        }
    }

}