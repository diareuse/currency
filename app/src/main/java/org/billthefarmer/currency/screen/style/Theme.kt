package org.billthefarmer.currency.screen.style

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CurrencyTheme(content: @Composable () -> Unit) = MaterialTheme(
    colors = currencyColors(),
    typography = currencyTypography(),
    shapes = currencyShapes(),
    content = content
)