// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.Dialog
import utils.DialogFile
import utils.RXTX
import windows.ConnectToBreakChecker
import windows.MainWindow
import windows.RobotConnectionWindow
import java.io.File
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    application {
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
        val isKRSDSave = remember { mutableStateOf(false) }
        val isKRSDLoad = remember { mutableStateOf(false) }

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
            report = report.value,
            onSave = {
                isKRSDSave.value = true
//                coroutineScope.launch(Dispatchers.IO) {
//                    var string = ""
//                    if (report.value != null) {
//                        string += Json.encodeToString(report.value)
//                    }
//
//                    string += "\n----------------------------------\n"
//
//                    if (robot.data != null) {
//                        string += Json.encodeToString(robot.data)
//                    }
//
//                    File("C:\\Users\\Artem\\IdeaProjects\\KRobotUtils\\src\\main\\resources\\fileName.krsd").bufferedWriter()
//                        .use { out -> out.write(string) }
//                }
            },
            onLoad = {
                isKRSDLoad.value = true
//                val str =
//                    File("C:\\Users\\Artem\\IdeaProjects\\KRobotUtils\\src\\main\\resources\\fileName.krsd").readText(
//                        Charsets.UTF_8
//                    )
//                val splt = str.split("----------------------------------")
//                report.value = Json.decodeFromString<Report>(splt[0])
//                robot.data = Json.decodeFromString<Data>(splt[1])
            }
        )

        val dataReadStatus = remember { MutableStateFlow("") }
        // Navigation
        when {
            isRobotConnecting.value -> {
                RobotConnectionWindow(
                    dataReadStatus = dataReadStatus,
                    onClose = { isRobotConnecting.value = false },
                    onConnect = { ip, port ->
                        coroutineScope.launch(Dispatchers.IO) {
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

            isKRSDSave.value -> {
                Window(
                    onCloseRequest = {isKRSDSave.value = false },
                ) {
                    DialogFile(
                        mode = Dialog.Mode.SAVE,
                        title = "Save KRSD File",
                        extensions = listOf(FileNameExtensionFilter("KRSD Files", "krsd"))
                    ) { filesDirect ->
                        coroutineScope.launch(Dispatchers.IO) {
                            var string = ""
                            if (report.value != null) {
                                string += Json.encodeToString(report.value)
                            }

                            string += "\n----------------------------------\n"

                            if (robot.data != null) {
                                string += Json.encodeToString(robot.data)
                            }

                            filesDirect[0].bufferedWriter().use { out -> out.write(string) }
                            isKRSDSave.value = false
                        }
                    }
                }
            }

            isKRSDLoad.value -> {
                Window(
                    onCloseRequest = { isKRSDLoad.value = false },
                ) {
                    DialogFile(
                        mode = Dialog.Mode.LOAD,
                        title = "Load KRSD File",
                        extensions = listOf(FileNameExtensionFilter("KRSD Files", "krsd"))
                    ) { filesDirect ->
                        val str = filesDirect[0].readText(Charsets.UTF_8)
                        val splt = str.split("----------------------------------")
                        report.value = Json.decodeFromString<Report>(splt[0])
                        robot.data = Json.decodeFromString<Data>(splt[1])
                        isKRSDLoad.value = false
                    }
                }
            }
        }

    }
}