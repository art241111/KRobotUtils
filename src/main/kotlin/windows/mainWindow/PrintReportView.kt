package windows.mainWindow

import KRobot
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.Report
import ui.Table

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrintReportView(
    modifier: Modifier = Modifier,
    robot: KRobot,
    report: Report?
) {
    LazyColumn(modifier) {
        item {
            HeaderData(kRobot = robot)
        }

        item {
            if (report != null) {
                ReportView(report, robot)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReportView(
    report: Report,
    robot: KRobot,
) {
    Card(
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        Column {
            PrintValueView("Checker No", report.checkerNo.toString())
            PrintValueView("SoftwareVersion", report.softwareVersion)
            PrintValueView("MachineType", report.machineType)
            PrintValueView("Separate Harness", report.separateHarness.toString())
            PrintValueView("Measured Ohm Judge", report.measuredOhmJudge.toString())

            robot.data?.let {
                val rowCount = it.axesCount / 2

                Table(
                    col = 2,
                    row = if (rowCount % 2 == 0) rowCount else rowCount + 1
                ) { col, row ->
                    val index = col * 2 + row
                    val reportJT = report.reportsJT[index]

                    if ((index + 1) <= it.axesCount) {
                        Card(
                            modifier = Modifier
                                .padding(4.dp),
                            elevation = 8.dp,
                        ) {
                            Column {
                                Text(
                                    text = "Ось JT${index + 1}",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center
                                )

                                PrintValueView("Attracting volts", reportJT.attractingVolts.meanData.toString())
                                PrintValueView("Releasing volts", reportJT.releasingVolts.meanData.toString())
                                PrintValueView("Brake resistance", reportJT.brakeResistance.measureData.toString())

                                if (robot.data?.motorsMoveTimes?.isNotEmpty() == true)
                                    robot.data?.motorsMoveTimes?.get(index)
                                        ?.let { PrintValueView("Время работы", it.toString()) }
                                if (robot.data?.motorsMoveAngles?.isNotEmpty() == true)
                                    robot.data?.motorsMoveAngles?.get(index)
                                        ?.let { PrintValueView("Смещение", it.toString()) }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun HeaderData(
    modifier: Modifier = Modifier,
    kRobot: KRobot
) {
    Column(modifier) {
        val data = kRobot.dataFlow.collectAsState()
        if (data.value != null) {
            PrintValueView("Robot type", data.value!!.robotType)
            PrintValueView("Serial number", data.value!!.serialNumber)
            PrintValueView("Uptime controller", data.value!!.uptimeController.toString())
            PrintValueView("Uptime servos", data.value!!.uptimeServo.toString())
            PrintValueView("Motor on counter", data.value!!.motorOnCounter.toString())
            PrintValueView("E-stop counter", data.value!!.eStopCounter.toString())
            PrintValueView("Brake counter", data.value!!.brakeCounter.toString())
        }
    }
}

@Composable
private fun PrintValueView(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text("$label: ")

        Text(value)
    }
}