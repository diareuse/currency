package wiki.depasquale.currency.screen.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CurrencyTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = currencyColors(),
    typography = CurrencyTypography,
    shapes = CurrencyShapes,
    content = content
)
