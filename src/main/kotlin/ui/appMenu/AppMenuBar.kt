package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import data.AppBarMenuItem
import data.AppBarMenuItemWithContext
import data.AppBarMenuList
import data.SimpleAppBarMenuItem
import ui.appMenu.MenuBarItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMenuBar(
    appBarMenuItems: List<AppBarMenuItem>,
    scope: FrameWindowScope,
    hidePanel: () -> Unit
) {
    Column(
        Modifier.background(MaterialTheme.colors.background)
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
                            hidePanel()
                        },
                        text = it.itemText
                    )
                }
            }
        }
    }


}