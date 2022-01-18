package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        Column(
            Modifier.background(MaterialTheme.colors.primaryVariant)
                .fillMaxHeight()
                .width(IntrinsicSize.Max)
        ) {
            appBarMenuItems.forEach {
                if (it is AppBarMenuList) {
                    it.list.forEach {
                        MenuBarItem(
                            onClick = {
                                if (it is SimpleAppBarMenuItem) {
                                    it.onClick()
                                } else if (it is AppBarMenuItemWithContext) {
                                    it.onClick(scope)
                                }
                            },
                            text = it.itemText
                        )
                    }
                }
            }
        }

        Divider(modifier = Modifier.width(1.dp).fillMaxHeight(), color = MaterialTheme.colors.secondary)
    }

}