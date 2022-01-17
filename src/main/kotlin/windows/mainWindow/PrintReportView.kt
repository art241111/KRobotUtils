package windows.mainWindow

import Data
import KRobot
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.Report
import ui.Table

@Composable
fun PrintReportView(
    modifier: Modifier = Modifier,
    robot: KRobot,
    report: Report?
) {
    val data = robot.dataFlow.collectAsState()
    LazyColumn(modifier) {
        if (data.value != null) {
            item {
                Column {
                    Text("Robot data")
                    HeaderData(data = data.value!!)
                }
            }
        }

        if (report != null) {
            item {
                Column {
                    Text("Break checker")
                    ReportView(report, data.value!!)
                }
            }
        }
    }
}

@Composable
private fun ReportView(
    report: Report,
    data: Data,
) {
    Box(
        modifier = Modifier.padding(10.dp).fillMaxWidth().border(1.dp, Color.Gray)
    ) {
        Column(
            Modifier.padding(10.dp)
        ) {
            PrintValueView("Checker No", report.checkerNo.toString())
            PrintValueView("SoftwareVersion", report.softwareVersion)
            PrintValueView("MachineType", report.machineType)
            PrintValueView("Separate Harness", report.separateHarness.toString())
            PrintValueView("Measured Ohm Judge", report.measuredOhmJudge.toString())


            Table(
                modifier = Modifier.fillMaxHeight(),
                col = 2,
                count = data.axesCount,
            ) { index ->
                val reportJT = report.reportsJT[index]

                Card(
                    modifier = Modifier
                        .padding(4.dp).weight(1f),
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

                        if (data.motorsMoveTimes.isNotEmpty())
                            PrintValueView("Время работы", data.motorsMoveTimes[index].toString())
                        if (data.motorsMoveAngles.isNotEmpty())
                            PrintValueView("Смещение", data.motorsMoveAngles[index].toString())
                    }
                }
            }
        }
    }
}


@Composable
private fun HeaderData(
    modifier: Modifier = Modifier,
    data: Data
) {
    Column(modifier) {
        PrintValueView("Robot type", data.robotType)
        PrintValueView("Serial number", data.serialNumber)
        PrintValueView("Uptime controller", data.uptimeController.toString())
        PrintValueView("Uptime servos", data.uptimeServo.toString())
        PrintValueView("Motor on counter", data.motorOnCounter.toString())
        PrintValueView("E-stop counter", data.eStopCounter.toString())
        PrintValueView("Brake counter", data.brakeCounter.toString())
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