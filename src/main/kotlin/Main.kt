// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import strings.S
import utils.RXTX
import utils.getReportData
import windows.ConnectToBreakChecker
import windows.RobotConnectionWindow

fun main() = application {
    val rxtx = remember { RXTX() }
    val reportNames = remember { mutableStateOf(listOf<String>()) }

    val coroutineScope = rememberCoroutineScope()
    val listAllowedPorts = remember { mutableStateOf(listOf<String>()) }
    val isFirstStart = remember { mutableStateOf(true) }
    if (isFirstStart.value) {
        isFirstStart.value = false
        coroutineScope.launch(Dispatchers.IO) {
            listAllowedPorts.value = rxtx.getAvailableSerialPorts()
        }
    }

    var isRobotConnecting by remember { mutableStateOf(false) }
    var isBreakCheckerConnecting by remember { mutableStateOf(false) }

    val robot = remember { KRobot(coroutineScope) }

    Window(
        onCloseRequest = ::exitApplication,
        title = S.strings.title,
        state = rememberWindowState(width = 900.dp, height = 600.dp)
    ) {

        MaterialTheme {
            Column {
                AppMenuBar(
                    onSave = {},
                    onRobotConnect = {
                        if (isRobotConnecting) {
                            robot.disconnect()
                        } else {
                            isRobotConnecting = true
                        }
                    },
                    onBreakCheckerConnect = { isBreakCheckerConnecting = true },
                )

                Row {
                    Button(onClick = {
                        if (!rxtx.isConnect.value) {
                            isBreakCheckerConnecting = true
                        } else {
                            rxtx.disconnect()
                        }
                    }) {
                        Text(if (!rxtx.isConnect.value) "Connect" else "Disconnect")
                    }
                }


                val selectedRepName = remember { mutableStateOf(0) }
                LazyColumn {
                    itemsIndexed(reportNames.value) { index, reportName ->
                        ui.ListItem(
                            onClick = {
                                selectedRepName.value = index
                            },
                            text = reportName,
                            isSelect = index == selectedRepName.value,
                            onDoubleClick = {
                                coroutineScope.launch {
                                    selectedRepName.value = index

                                    val report = rxtx.getReportData(coroutineScope, selectedRepName.value + 1)
                                    println(report)
                                }
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val report = rxtx.getReportData(coroutineScope, selectedRepName.value + 1)
                            println(report)
                        }
                    }
                ) {
                    Text("Get report")
                }

            }
        }
    }

    when {
        isRobotConnecting -> {
            RobotConnectionWindow(
                onCloseRequest = { isRobotConnecting = false },
                onConnect = { ip, port ->
                    robot.connect(ip, port)
                }
            )
        }

        isBreakCheckerConnecting -> {
            ConnectToBreakChecker(
                onClose = {
                    isBreakCheckerConnecting = false
                },
                listAllowedPorts = listAllowedPorts,
                coroutineScope = coroutineScope,
                rxtx = rxtx,
                reportNames = reportNames
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMenuBar(
    onSave: () -> Unit,
    onRobotConnect: () -> Unit,
    onBreakCheckerConnect: () -> Unit,
) {
    Row {
        MenuBarItem(
            text = S.strings.save,
            onClick = onSave
        )

        MenuBarItem(
            text = S.strings.toRobot,
            onClick = onRobotConnect
        )

        MenuBarItem(
            text = S.strings.toBreakChecker,
            onClick = onBreakCheckerConnect
        )
    }
}

/**
 *
 * Without the content parameter, the menu becomes a simple button.
 */
@Composable
private fun MenuBarItem(
    text: String = "",
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = {
            onClick()
            expanded = !expanded
        }) {
            Text(text)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            }
        ) {
            content()
        }
    }


}