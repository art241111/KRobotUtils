package windows

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.SimpleAppBarMenuItem
import data.AppBarMenuItemWithContext
import data.Report
import data.AppBarMenuItem
import strings.S
import ui.AppMenuBar
import windows.mainWindow.PrintReportView

@Composable
fun MainWindow(
    onClose: () -> Unit,
    robot: KRobot,
    report: Report?,
    appBarMenuItems: List<AppBarMenuItem>,
) = Window(
    onCloseRequest = onClose,
    title = S.strings.title,
    state = rememberWindowState(width = 900.dp, height = 600.dp)
) {
    val scope = this
    val appBarMenuItemsAll = mutableListOf<AppBarMenuItem>()

//    appBarMenuItems.forEach { item ->
//        if (item is AppBarMenuItemWithContext) {
//            appBarMenuItemsAll.add(
//                SimpleAppBarMenuItem(
//                    itemText = item.itemText,
//                    onClick = {
//                        item.onClick(scope)
//                    }
//                )
//            )
//        } else {
//            appBarMenuItemsAll.add(item)
//        }
//    }

    MaterialTheme {
        Column {
            AppMenuBar(appBarMenuItems, scope)

            PrintReportView(kRobot = robot, report = report)
        }
    }
}
