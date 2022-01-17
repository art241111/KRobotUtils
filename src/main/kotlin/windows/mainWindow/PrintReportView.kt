package windows.mainWindow

import KRobot
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.Report

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrintReportView(
    modifier: Modifier = Modifier,
    robot: KRobot,
    report: Report?
) {
    Column(modifier) {
        HeaderData(kRobot = robot)

        if (report != null) {
            ReportView(report, robot)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReportView(
    report: Report,
    robot: KRobot
) {
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column {
            PrintValueView("Checker No", report.checkerNo.toString())
            PrintValueView("SoftwareVersion", report.softwareVersion)
            PrintValueView("MachineType", report.machineType)
            PrintValueView("Separate Harness", report.separateHarness.toString())
            PrintValueView("Measured Ohm Judge", report.measuredOhmJudge.toString())

            LazyVerticalGrid(
                cells = GridCells.Adaptive(300.dp)
            ) {
                itemsIndexed(report.reportsJT) { index, reportJT ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp),
                        elevation = 8.dp,
                    ) {
                        Column {
                            Text(
                                text = "Ось JT${index + 1}",
                                modifier = Modifier.fillParentMaxWidth(),
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

@Composable
private fun HeaderData(
    modifier: Modifier = Modifier,
    kRobot: KRobot
) {
    Column(modifier) {
        if (kRobot.data != null) {
            PrintValueView("Robot type", kRobot.data!!.robotType)
            PrintValueView("Uptime controller", kRobot.data!!.uptimeController.toString())
            PrintValueView("Uptime servos", kRobot.data!!.uptimeServo.toString())
            PrintValueView("Motor on counter", kRobot.data!!.motorOnCounter.toString())
            PrintValueView("E-stop counter", kRobot.data!!.eStopCounter.toString())
            PrintValueView("Brake counter", kRobot.data!!.brakeCounter.toString())
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