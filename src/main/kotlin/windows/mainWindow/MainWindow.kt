package windows.mainWindow

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.Report
import data.AppBarMenuItem
import strings.S
import ui.AppMenuBar
import ui.titleBar.AppWindowTitleBar

@Composable
fun MainWindow(
    onClose: () -> Unit,
    robot: KRobot,
    report: Report?,
    appBarMenuItems: List<AppBarMenuItem>,
) {
    Window(
        onCloseRequest = onClose,
        title = S.strings.title,
        state = rememberWindowState(width = 1000.dp, height = 600.dp),
        undecorated = true,
//        alwaysOnTop = true
    ) {
        val scope = this
        val isShowMenu = remember { mutableStateOf(false) }

        MaterialTheme {
            Column {
                AppWindowTitleBar(
                    onClose = onClose,
                    scope = scope,
                    onOpenMenu = { isShowMenu.value = true },
                    onCloseMenu = { isShowMenu.value = false }
                )
                if (isShowMenu.value)
                    AppMenuBar(appBarMenuItems, scope)

                PrintReportView(robot = robot, report = report)
            }
        }
    }
}
