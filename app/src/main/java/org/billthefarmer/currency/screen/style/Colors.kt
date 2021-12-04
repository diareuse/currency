package org.billthefarmer.currency.screen.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun currencyColors() = when (isSystemInDarkTheme()) {
    true -> currencyColorsDark()
    false -> currencyColorsLight()
}

@Composable
private fun currencyColorsLight() = Colors(
    primary = Color(0xFF_333042),
    primaryVariant = Color(0xFF_d31e21),
    secondary = Color(0xFF_f7a52d),
    secondaryVariant = Color(0xFF_f7a52d),
    background = Color(0xFF_f1f1f1),
    surface = Color(0xFF_ffffff),
    error = Color(0xFF_ff4000),
    onPrimary = Color(0xFF_ffffff),
    onSecondary = Color(0xFF_000000),
    onBackground = Color(0xFF_000000),
    onSurface = Color(0xFF_000000),
    onError = Color(0xFF_ffffff),
    isLight = true,
)

@Composable
private fun currencyColorsDark() = Colors(
    primary = Color(0xFF_e6f4cd),
    primaryVariant = Color(0xFF_75bbae),
    secondary = Color(0xFF_1b83a2),
    secondaryVariant = Color(0xFF_1b83a2),
    background = Color(0xFF_0f0f0f),
    surface = Color(0xFF_000000),
    error = Color(0xFF_e43030),
    onPrimary = Color(0xFF_000000),
    onSecondary = Color(0xFF_ffffff),
    onBackground = Color(0xFF_ffffff),
    onSurface = Color(0xFF_ffffff),
    onError = Color(0xFF_ffffff),
    isLight = false,
)