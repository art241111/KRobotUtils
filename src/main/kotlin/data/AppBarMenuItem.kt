package data

import androidx.compose.ui.window.FrameWindowScope

interface AppBarMenuItem {
    val itemText: String
}

class SimpleAppBarMenuItem(
    override val itemText: String,
    val onClick: () -> Unit,
) : AppBarMenuItem

class AppBarMenuItemWithContext(
    override val itemText: String,
    val onClick: (FrameWindowScope) -> Unit
) : AppBarMenuItem

class AppBarMenuList(
    override val itemText: String,
    val list: List<AppBarMenuItem>
) : AppBarMenuItem
