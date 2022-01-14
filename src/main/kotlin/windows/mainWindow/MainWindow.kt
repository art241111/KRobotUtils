package windows

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.Report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import strings.S
import ui.AppMenuBar
import ui.RWList
import utils.RXTX
import utils.getReportData
import windows.mainWindow.PrintReportView

@Composable
fun MainWindow(
    onClose: () -> Unit,
    showRobotConnectionWindow: () -> Unit,
    showBreakCheckerConnectionWindow: () -> Unit,
    rxtx: RXTX,
    robot: KRobot,
    report: Report?,
    onSave: (FrameWindowScope) -> Unit,
    onLoad: (FrameWindowScope) -> Unit,
) = Window(
    onCloseRequest = onClose,
    title = S.strings.title,
    state = rememberWindowState(width = 900.dp, height = 600.dp)
) {
    val scope = this
    MaterialTheme {
        Column {
            AppMenuBar(
                onLoad = { onLoad(scope) },
                onSave = {
                    onSave(scope)
                },
                onRobotConnect = {
                    if (robot.isConnect.value) {
                        robot.disconnect()
                    } else {
                        showRobotConnectionWindow()
                    }
                },
                onBreakCheckerConnect = {
                    if (!rxtx.isConnect.value) {
                        showBreakCheckerConnectionWindow()
                    } else {
                        rxtx.disconnect()
                    }
                },
                breakCheckerStatus = if (!rxtx.isConnect.value) S.strings.connect else S.strings.disconnect
            )

            PrintReportView(kRobot = robot, report = report)
        }
    }
}