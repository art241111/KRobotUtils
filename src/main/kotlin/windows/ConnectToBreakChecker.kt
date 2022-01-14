package windows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import data.Report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import strings.S
import ui.RWList
import ui.rwList.ListWithProgressBar
import utils.RXTX
import utils.getReportData
import utils.getReportNames

@Composable
fun ConnectToBreakChecker(
    listAllowedPorts: MutableState<List<String>>,
    coroutineScope: CoroutineScope,
    rxtx: RXTX,
    reportNames: MutableState<List<String>>,
    onClose: () -> Unit,
    reports: MutableState<Report?>
) {
    Window(
        onCloseRequest = { onClose() },
        state = rememberWindowState(width = 300.dp, height = 400.dp),
        title = S.strings.breakCheckerWindowName
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = S.strings.listAcceptablePorts,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            if (!rxtx.isConnect.value) {
                // Output a list of ports
                ListWithProgressBar(listAllowedPorts.value, S.strings.connect) { index ->
                    coroutineScope.launch {
                        rxtx.connect(listAllowedPorts.value[index], coroutineScope)
                        reportNames.value = rxtx.getReportNames()
                    }
                }
            } else {
                ListWithProgressBar(reportNames.value, S.strings.getReport) { index ->
                    coroutineScope.launch {
                        onClose()
                        val report = rxtx.getReportData(coroutineScope, index + 1)
                        reports.value = report
                        println(report)
                    }
                }
            }
        }
    }
}