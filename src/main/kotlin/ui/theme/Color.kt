package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Red200 = Color(255,130,135)
val Red500 = Color(255,80,86)
val Red700 = Color(151,0,3)

val LightGray = Color(243,246,244)
internal val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Color.DarkGray,
    secondary = Color.White,
    surface = Color.LightGray
)

internal val LightColorPalette = lightColors(
    primary = Red700,
    primaryVariant = Color.White,
    secondary = Color.DarkGray,
)
