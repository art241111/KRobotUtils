package windows.mainWindow

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
        state = rememberWindowState(width = 900.dp, height = 600.dp),
        undecorated = true,
        alwaysOnTop = true
    ) {
        val scope = this

        MaterialTheme {
            Column {
                AppWindowTitleBar(onClose)

                AppMenuBar(appBarMenuItems, scope)

                PrintReportView(robot = robot, report = report)
            }
        }
    }
}
