package cursola.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeRateItemInline(
    flag: @Composable () -> Unit,
    content: @Composable (flagSize: IntSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        var flagSize by remember { mutableStateOf(IntSize.Zero) }
        content(flagSize)
        Box(
            modifier = Modifier
                .padding(12.dp)
                .onSizeChanged { flagSize = it }
        ) {
            flag()
        }
    }
}