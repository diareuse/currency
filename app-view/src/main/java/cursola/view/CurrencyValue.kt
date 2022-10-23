package cursola.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max

@Composable
fun CurrencyValue(value: String, symbol: String, modifier: Modifier = Modifier) {
    val text = remember(value, symbol) {
        buildAnnotatedString {
            append(value)
            val index = value.indexOf(symbol)
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), 0, max(0, index - 1))
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), index + symbol.length, length)
        }
    }
    Text(
        text = text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme {
        CurrencyValue(value = "$123.22", symbol = "$")
    }
}