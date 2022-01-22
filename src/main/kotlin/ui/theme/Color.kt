package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Red200 = Color(238, 104, 109)
val Red500 = Color(255, 80, 86)
val Red700 = Color(151, 0, 3)

val LightRed = Color(249, 245, 245)

val LightGray = Color(243, 246, 244)
internal val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.LightGray,
    secondary = Color.White,
    surface = Color.Gray,
    background = Color.DarkGray
)

internal val LightColorPalette = lightColors(
    primary = Red700,
    primaryVariant = Red500,
    secondary = Color.Black,
    surface = Color.White,
    background = LightRed
)
