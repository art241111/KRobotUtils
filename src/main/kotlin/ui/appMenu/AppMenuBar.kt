package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import data.AppBarMenuItem
import data.AppBarMenuItemWithContext
import data.AppBarMenuList
import data.SimpleAppBarMenuItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMenuBar(
    appBarMenuItems: List<AppBarMenuItem>,
    scope: FrameWindowScope
) {
    Column {
        Divider(Modifier.fillMaxWidth())
        Row() {
            appBarMenuItems.forEach {
                val expanded = remember { mutableStateOf(false) }

                if (it is AppBarMenuList) {
                    Box {
                        MenuBarItem(
                            text = it.itemText,
                            onClick = {
                                expanded.value = true
                            }
                        )

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            it.list.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded.value = false

                                        if (it is SimpleAppBarMenuItem) {
                                            it.onClick()
                                        } else if (it is AppBarMenuItemWithContext) {
                                            it.onClick(scope)
                                        }
                                    }
                                ) {
                                    Text(it.itemText)
                                }
                            }
                        }
                    }

                }


            }
        }

        Divider(Modifier.fillMaxWidth())
    }

}