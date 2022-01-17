// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.application
import data.AppBarMenuItemWithContext
import data.AppBarMenuList
import data.Report
import data.SimpleAppBarMenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.poi.ss.usermodel.Sheet
import strings.S
import utils.Dialog
import utils.DialogFile
import utils.RXTX
import utils.excelUtils.setValue
import utils.excelUtils.workbook
import utils.excelUtils.write
import windows.ConnectToBreakChecker
import windows.mainWindow.MainWindow
import windows.RobotConnectionWindow
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
        val isKRSDSave = remember { mutableStateOf<FrameWindowScope?>(null) }
        val isReportSave = remember { mutableStateOf<FrameWindowScope?>(null) }
        val isBackupSave = remember { mutableStateOf<FrameWindowScope?>(null) }
        val isKRSDLoad = remember { mutableStateOf<FrameWindowScope?>(null) }
        val isFromBackUpLoad = remember { mutableStateOf<FrameWindowScope?>(null) }

        val robot = remember { KRobot(coroutineScope) }

        val appBarMenuItems = listOf(
            AppBarMenuList(
                itemText = S.strings.load,
                list = listOf(
                    AppBarMenuItemWithContext(
                        itemText = "Загрузка с диска (.krsd)",
                        onClick = { scope ->
                            isKRSDLoad.value = scope
                        }
                    ),
                    AppBarMenuItemWithContext(
                        itemText = "Загрузка с бэкапа (.as)",
                        onClick = { scope ->
                            isFromBackUpLoad.value = scope
                        }
                    ),
                    SimpleAppBarMenuItem(
                        itemText = S.strings.toRobot,
                        onClick = {
                            if (robot.isConnect.value) {
                                robot.disconnect()
                            } else {
                                isRobotConnecting.value = true
                            }
                        }
                    ),
                    SimpleAppBarMenuItem(
                        itemText = S.strings.toBreakChecker,
                        onClick = {
                            if (!rxtx.isConnect.value) {
                                isBreakCheckerConnecting.value = true
                            } else {
                                rxtx.disconnect()
                            }
                        }
                    )
                )
            ),

            AppBarMenuList(
                itemText = S.strings.save,
                list = listOf(
                    AppBarMenuItemWithContext(
                        itemText = S.strings.saveProject,
                        onClick = { scope ->
                            isKRSDSave.value = scope
                        }
                    ),
                    AppBarMenuItemWithContext(
                        itemText = S.strings.saveExcelTable,
                        onClick = { scope ->
                            isReportSave.value = scope
                        }
                    ),
                    AppBarMenuItemWithContext(
                        itemText = S.strings.saveBackup,
                        onClick = { scope ->
                            isBackupSave.value = scope
                        }
                    ),
                )
            )
        )


        // Main window
        MainWindow(
            appBarMenuItems = appBarMenuItems,
            onClose = ::exitApplication,
            robot = robot,
            report = report.value,
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

            isKRSDSave.value != null -> {
                DialogFile(
                    mode = Dialog.Mode.SAVE,
                    title = "Save KRSD File",
                    extensions = listOf(FileNameExtensionFilter("KRSD Files", "krsd")),
                    scope = isKRSDSave.value!!
                ) { filesDirect ->
                    if (filesDirect.isNotEmpty()) {
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
                            isKRSDSave.value = null
                        }
                    }
                }
            }

            isKRSDLoad.value != null -> {
                DialogFile(
                    mode = Dialog.Mode.LOAD,
                    title = "Load KRSD File",
                    extensions = listOf(FileNameExtensionFilter("KRSD Files", "krsd")),
                    scope = isKRSDLoad.value!!
                ) { filesDirect ->
                    if (filesDirect.isNotEmpty()) {
                        val str = filesDirect[0].readText(Charsets.UTF_8)
                        val splt = str.split("----------------------------------")
                        report.value = Json.decodeFromString<Report>(splt[0])
                        robot.data = Json.decodeFromString<Data>(splt[1])
                    }
                }
                isKRSDLoad.value = null
            }

            isReportSave.value != null -> {
                DialogFile(
                    mode = Dialog.Mode.SAVE,
                    title = "Save report (.xlsx)",
                    extensions = listOf(FileNameExtensionFilter("Excel file", "xlsx")),
                    scope = isReportSave.value!!
                ) { filesDirect ->
                    if (filesDirect.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            workbook("mainProtocol.xlsx") {
                                val sheet: Sheet = getSheetAt(0)
                                if (robot.data != null) {
                                    with(robot.data!!) {
                                        // Add header
                                        sheet.setValue(4, 9, robotType)
                                        sheet.setValue(11, 9, serialNumber)
                                        sheet.setValue(9, 12, uptimeController.toString())
                                        sheet.setValue(9, 13, uptimeServo.toString())

                                        // Add axes
                                        for (i in 0..2) {
                                            val sdvig = i * 9
                                            sheet.setValue(3, 16 + sdvig, motorsMoveTimes[i].toString())
                                            sheet.setValue(3, 17 + sdvig, motorsMoveAngles[i].toString())
                                            if (report.value != null) {
                                                sheet.setValue(
                                                    4,
                                                    20 + sdvig,
                                                    report.value!!.reportsJT[i].brakeResistance.measureData.toString()
                                                )
                                                sheet.setValue(
                                                    4,
                                                    21 + sdvig,
                                                    report.value!!.reportsJT[i].attractingVolts.meanData.toString()
                                                )
                                                sheet.setValue(
                                                    4,
                                                    22 + sdvig,
                                                    report.value!!.reportsJT[i].releasingVolts.meanData.toString()
                                                )
                                            }
                                        }


                                        // Add axes
                                        for (i in 0..2) {
                                            val sdvig = i * 9
                                            sheet.setValue(15, 6 + sdvig, motorsMoveTimes[3 + i].toString())
                                            sheet.setValue(15, 7 + sdvig, motorsMoveAngles[3 + i].toString())
                                            if (report.value != null) {
                                                sheet.setValue(
                                                    16,
                                                    10 + sdvig,
                                                    report.value!!.reportsJT[3 + i].brakeResistance.measureData.toString()
                                                )
                                                sheet.setValue(
                                                    16,
                                                    11 + sdvig,
                                                    report.value!!.reportsJT[3 + i].attractingVolts.meanData.toString()
                                                )
                                                sheet.setValue(
                                                    16,
                                                    12 + sdvig,
                                                    report.value!!.reportsJT[3 + i].releasingVolts.meanData.toString()
                                                )
                                            }
                                        }


                                    }
                                }
                            }.write(filesDirect[0].absolutePath)

                            isReportSave.value = null
                        }
                    }
                }
            }

            isBackupSave.value != null -> {
                DialogFile(
                    mode = Dialog.Mode.SAVE,
                    title = "Save backup (.as)",
                    extensions = listOf(FileNameExtensionFilter("As file", "as")),
                    scope = isBackupSave.value!!
                ) { filesDirect ->
                    if (filesDirect.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            filesDirect[0].printWriter().use { out ->
                                robot.data?.backup?.forEach {
                                    out.print(it)
                                }
                            }
                            isBackupSave.value = null
                        }
                    }
                }

            }

            isFromBackUpLoad.value != null -> {
                DialogFile(
                    mode = Dialog.Mode.LOAD,
                    title = "Load from backup (.as)",
                    extensions = listOf(FileNameExtensionFilter("As file", "as")),
                    scope = isFromBackUpLoad.value!!
                ) { filesDirect ->
                    if (filesDirect.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            val str = filesDirect[0].readText(Charsets.UTF_8).split("\n")
                            robot.getDataFromBackup(str)
                        }

                        isFromBackUpLoad.value = null
                    }
                }
            }
        }

    }
}