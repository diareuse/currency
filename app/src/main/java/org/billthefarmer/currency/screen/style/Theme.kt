package org.billthefarmer.currency.screen.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CurrencyTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = currencyColors(),
    typography = CurrencyTypography,
    content = content
)

@Deprecated("", replaceWith = ReplaceWith("MaterialTheme.colorScheme"))
val MaterialTheme.colors
    @Composable get() = colorScheme