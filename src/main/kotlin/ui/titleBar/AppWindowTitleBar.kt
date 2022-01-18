package ui.titleBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import compose.icons.CssGgIcons
import compose.icons.cssggicons.Close
import compose.icons.cssggicons.CloseO
import compose.icons.cssggicons.MathMinus
import compose.icons.cssggicons.Maximize
import compose.icons.cssggicons.MenuOreos
import compose.icons.cssggicons.Minimize

@Composable
fun WindowScope.AppWindowTitleBar(
    onClose: () -> Unit,
    scope: FrameWindowScope,
    onOpenMenu: () -> Unit,
    onCloseMenu: () -> Unit
) = WindowDraggableArea {
    Column {
        Box(Modifier.fillMaxWidth().height(48.dp).background(MaterialTheme.colors.primaryVariant)) {
            Menu(Modifier.align(Alignment.CenterStart), onOpenMenu, onCloseMenu)
            Title(Modifier.align(Alignment.Center), "Robowizard")
            TitleBatButtons(Modifier.align(Alignment.CenterEnd), onClose, scope)
        }

        Divider(Modifier.fillMaxWidth(), Color.DarkGray)
    }
}

@Composable
private fun Menu(modifier: Modifier, onOpenMenu: () -> Unit, onCloseMenu: () -> Unit) {
    val isOpen = remember { mutableStateOf(false) }
    IconButton(modifier = modifier, onClick = {
        if (!isOpen.value) {
            isOpen.value = true
            onOpenMenu()
        } else {
            isOpen.value = false
            onCloseMenu()
        }
    }) {
        if (!isOpen.value) {
            Icon(CssGgIcons.MenuOreos, "Menu", tint = MaterialTheme.colors.secondary)
        } else {
            Icon(CssGgIcons.Close, "Close menu", tint = MaterialTheme.colors.secondary)
        }
    }
}

@Composable
private fun Title(modifier: Modifier, text: String) {
    Text(modifier = modifier, text = text, color = MaterialTheme.colors.secondary)
}

@Composable
private fun TitleBatButtons(modifier: Modifier, onClose: () -> Unit, scope: FrameWindowScope) {
    val windowSize = remember { mutableStateOf(WindowPlacement.Floating) }

    Row(modifier) {
        IconButton(
            onClick = {
                // TODO: Add
            }
        ) {
            Icon(CssGgIcons.MathMinus, "-", tint = MaterialTheme.colors.secondary)
        }

        val icon = remember(windowSize.value) {
            if (windowSize.value != WindowPlacement.Fullscreen) CssGgIcons.Maximize else CssGgIcons.Minimize
        }
        IconButton(
            onClick = {
                if (windowSize.value == WindowPlacement.Floating) {
                    scope.window.placement = WindowPlacement.Fullscreen
                    windowSize.value = WindowPlacement.Fullscreen
                } else {
                    scope.window.placement = WindowPlacement.Floating
                    windowSize.value = WindowPlacement.Floating
                }
            }
        ) {
            Icon(icon, "o", tint = MaterialTheme.colors.secondary)
        }

        IconButton(
            onClick = onClose
        ) {
            Icon(CssGgIcons.Close, "x", tint = MaterialTheme.colors.secondary)
        }
    }
}