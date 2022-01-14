package windows.mainWindow

import KRobot
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.Report

@Composable
fun PrintReportView(
    modifier: Modifier = Modifier,
    kRobot: KRobot,
    report: Report?
) {
    Column (modifier) {
        HeaderData(kRobot = kRobot)
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