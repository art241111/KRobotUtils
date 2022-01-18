package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable


@Composable
fun AppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = AppTypography,
        shapes = Shapes(),
        content = content
    )
}
