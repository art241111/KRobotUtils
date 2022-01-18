package ui.titleBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.AppWindowTitleBar(onClose: () -> Unit) = WindowDraggableArea {
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray)) {
        Row  (Modifier.align(Alignment.CenterEnd)){
            IconButton(
                onClick = onClose
            ) {
                Icon(Icons.Default.Close, "x", tint = Color.White)
            }

        }
    }
}