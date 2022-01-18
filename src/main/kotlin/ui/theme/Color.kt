package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Red700 = Color(151,0,3)

internal val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.DarkGray,
    secondary = Color.White,
    surface = Color.Gray,
)

internal val LightColorPalette = lightColors(
    primary = Red700,
    primaryVariant = Color.White,
    secondary = Color.DarkGray,
)
