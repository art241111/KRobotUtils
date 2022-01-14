package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 *
 * Without the content parameter, the menu becomes a simple button.
 */
@Composable
fun MenuBarItem(
    text: String = "",
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = {
            onClick()
            expanded = !expanded
        }) {
            Text(text)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            }
        ) {
            content()
        }
    }
}