package wiki.depasquale.currency.screen.style

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun CurrencyTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = currencyColors(),
    typography = CurrencyTypography,
    shapes = CurrencyShapes
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        content()
    }
}
