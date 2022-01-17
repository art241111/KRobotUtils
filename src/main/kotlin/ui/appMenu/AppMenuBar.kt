package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
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
    Row {
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
                                }
                            ) {
                                if (it is SimpleAppBarMenuItem) {
                                    MenuBarItem(
                                        text = it.itemText,
                                        onClick = {
                                            it.onClick()
                                            expanded.value = false
                                        }
                                    )
                                } else if (it is AppBarMenuItemWithContext) {
                                    MenuBarItem(
                                        text = it.itemText,
                                        onClick = {
                                            it.onClick(scope)
                                            expanded.value = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            }


        }
    }
}