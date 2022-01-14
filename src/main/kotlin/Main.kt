// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import data.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import utils.RXTX
import windows.ConnectToBreakChecker
import windows.MainWindow
import windows.RobotConnectionWindow

fun main() = application {
    // Init data
    val rxtx = remember { RXTX() }
    val reportNames = remember { mutableStateOf(listOf<String>()) }
    val report = remember { mutableStateOf<Report?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val listAllowedPorts = remember { mutableStateOf(listOf<String>()) }
    val isFirstStart = remember { mutableStateOf(true) }
    if (isFirstStart.value) {
        isFirstStart.value = false
        coroutineScope.launch(Dispatchers.IO) {
            listAllowedPorts.value = rxtx.getAvailableSerialPorts()
        }
    }

    val isRobotConnecting = remember { mutableStateOf(false) }
    val isBreakCheckerConnecting = remember { mutableStateOf(false) }

    val robot = remember { KRobot(coroutineScope) }

    // Main window
    MainWindow(
        onClose = ::exitApplication,
        showRobotConnectionWindow = {
            isRobotConnecting.value = true
        },
        showBreakCheckerConnectionWindow = {
            isBreakCheckerConnecting.value = true
        },
        rxtx = rxtx,
        robot = robot,
        report = report.value
    )

    val dataReadStatus = remember { MutableStateFlow("") }
    // Navigation
    when {
        isRobotConnecting.value -> {
            RobotConnectionWindow(
                dataReadStatus = dataReadStatus,
                onClose = { isRobotConnecting.value = false },
                onConnect = { ip, port ->
                    coroutineScope.launch (Dispatchers.IO) {
                        robot.connect(ip, port, dataReadStatus)
                        isRobotConnecting.value = false
                    }
                }
            )
        }

        isBreakCheckerConnecting.value -> {
            ConnectToBreakChecker(
                onClose = {
                    isBreakCheckerConnecting.value = false
                },
                listAllowedPorts = listAllowedPorts,
                coroutineScope = coroutineScope,
                rxtx = rxtx,
                reportNames = reportNames,
                reports = report
            )
        }
    }

}