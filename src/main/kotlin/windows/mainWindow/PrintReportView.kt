package windows.mainWindow

import Data
import KRobot
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import data.Report
import ui.Table
import ui.theme.LightGray
import ui.theme.Red200
import ui.theme.Red500

@Composable
fun PrintReportView(
    modifier: Modifier = Modifier,
    robot: KRobot,
    report: Report?
) {
    val data = robot.dataFlow.collectAsState()
    Box(Modifier.fillMaxSize().background(MaterialTheme.colors.surface)) {
        LazyColumn(modifier) {
            if (data.value != null) {
                item {
                    Column {
                        Spacer(Modifier.height(10.dp))
                        HeaderText("Robot data")
                        Spacer(Modifier.height(10.dp))
                        HeaderData(data = data.value!!)
                    }
                }
            }

            if (report != null) {
                item {
                    Column {
                        Spacer(Modifier.height(10.dp))
                        HeaderText("Break checker")
                        ReportView(report, data.value!!)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = MaterialTheme.typography.h1.fontSize ) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = text,
        fontStyle = MaterialTheme.typography.h1.fontStyle,
        fontWeight = MaterialTheme.typography.h1.fontWeight,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.secondary
    )
}

@Composable
private fun ReportView(
    report: Report,
    data: Data,
) {
    Box(
        modifier = Modifier.padding(5.dp).fillMaxWidth()
//            .background(LightGray, shape = RoundedCornerShape(10.dp))
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

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Red200, Red500)
                            ),
                            shape = RoundedCornerShape(15.dp),
                        )

                ) {
                    Column(Modifier.padding(10.dp)) {
                        PrintValueView(
                            label = "Ось JT",
                            value = (index + 1).toString(),
                            color = Color.White
                        )
                        PrintValueView(
                            label = "Attracting volts",
                            value = reportJT.attractingVolts.meanData.toString(),
                            color = Color.White
                        )
                        PrintValueView(
                            label = "Releasing volts",
                            value = reportJT.releasingVolts.meanData.toString(),
                            color = Color.White
                        )
                        PrintValueView(
                            label = "Brake resistance",
                            value = reportJT.brakeResistance.measureData.toString(),
                            color = Color.White
                        )

                        if (data.motorsMoveTimes.isNotEmpty())
                            PrintValueView(
                                label = "Время работы",
                                value = data.motorsMoveTimes[index].toString(),
                                color = Color.White
                            )
                        if (data.motorsMoveAngles.isNotEmpty())
                            PrintValueView(
                                label = "Смещение",
                                value = data.motorsMoveAngles[index].toString(),
                                color = Color.White
                            )
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
    Column(modifier.padding(horizontal = 14.dp)) {
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
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.secondary
) {
    Row(modifier) {
        Text("$label: $value", color = color)
    }
}