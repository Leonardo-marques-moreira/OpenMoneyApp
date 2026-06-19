package com.openmoney.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OpenMoneyColorScheme = darkColorScheme(
    primary = OpenMoneyGreen,
    onPrimary = Color.White,
    secondary = OpenMoneyTextSecondary,
    background = OpenMoneyBackground,
    onBackground = OpenMoneyTextPrimary,
    surface = OpenMoneySurface,
    onSurface = OpenMoneyTextPrimary,
    outline = OpenMoneyOutline,
)

@Composable
fun OpenMoneyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OpenMoneyColorScheme,
        typography = Typography,
        content = content
    )
}
