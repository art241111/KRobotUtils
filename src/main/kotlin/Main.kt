// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import strings.S
import ui.AppMenuBar
import ui.RWList
import utils.RXTX
import utils.getReportData
import windows.ConnectToBreakChecker
import windows.MainWindow
import windows.RobotConnectionWindow

fun main() = application {
    // Init data
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

    val isRobotConnecting = remember { mutableStateOf(false) }
    val isBreakCheckerConnecting = remember { mutableStateOf(false) }

    val robot = remember { KRobot(coroutineScope) }

    // Main window
    MainWindow(
        onClose = ::exitApplication,
        rxtx = rxtx,
        robot = robot,
        coroutineScope = coroutineScope,
        reportNames = reportNames.value,
        showRobotConnectionWindow = {
            isRobotConnecting.value = true
        },
        showBreakCheckerConnectionWindow = {
            isBreakCheckerConnecting.value = true
        }
    )

    // Navigation
    when {
        isRobotConnecting.value -> {
            RobotConnectionWindow(
                onCloseRequest = { isRobotConnecting.value = false },
                onConnect = { ip, port ->
                    robot.connect(ip, port)
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
                reportNames = reportNames
            )
        }
    }

}