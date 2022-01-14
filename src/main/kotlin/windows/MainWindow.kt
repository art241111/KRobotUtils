package windows

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import strings.S
import ui.AppMenuBar
import ui.RWList
import utils.RXTX
import utils.getReportData

@Composable
fun MainWindow(
    onClose: () -> Unit,
    showRobotConnectionWindow: () -> Unit,
    showBreakCheckerConnectionWindow: () -> Unit,
    rxtx: RXTX,
    robot: KRobot,
    reportNames: List<String>,
    coroutineScope: CoroutineScope
) {
    Window(
        onCloseRequest = onClose,
        title = S.strings.title,
        state = rememberWindowState(width = 900.dp, height = 600.dp)
    ) {

        MaterialTheme {
            Column {
                AppMenuBar(
                    onSave = {},
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

                RWList(
                    items = reportNames,
                    onSelect = { index ->
                        coroutineScope.launch {
                            val report = rxtx.getReportData(coroutineScope, index + 1)
                            println(report)
                        }
                    },
                    buttonText = "Get report"
                )
            }
        }
    }
}