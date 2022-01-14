package utils.rxtxExtensions


import data.reportJT.AttractingVolts
import data.reportJT.BrakeResistance
import data.reportJT.ReleasingVolts
import data.Report
import data.reportJT.ReportJT
import data.reportJT.ResistanceLine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import utils.RXTX
import utils.codeToInt
import utils.codeToIntWithCheck
import utils.codeToJudgeResult
import utils.codeToLong
import utils.codeToLongWithCheck
import utils.getString

suspend fun RXTX.getReportData(coroutineScope: CoroutineScope, reportNumber: Int): Report {
    // Send requests to get the reports data
    var report: Report? = null
    coroutineScope.launch {
        report = Report(
            checkerNo = sendRequest(reportNumber, "19890").codeToIntWithCheck(),
            softwareVersion = sendRequest(reportNumber, "19880").substring(5, 9),
            machineType = sendRequest(reportNumber, "19600").getString(),
            separateHarness = sendRequest(reportNumber, "19640").codeToInt() / 10,
            measuredOhmJudge = sendRequest(reportNumber, "19620").codeToInt() / 10,
            reportsJT = getReportJT(reportNumber)
        )
    }.join()

    return report!!
}

private suspend fun RXTX.getReportJT(reportNumber: Int): List<ReportJT> {
    val reports = mutableListOf<ReportJT>()
    for (i in 0..7) {
        reports.add(
            ReportJT(
                resistance = getResistance(reportNumber, i),
                brakeVoltMeasuringTime = sendRequest(reportNumber, "230$i").codeToIntWithCheck(),
                attractingVolts = getAttractingVolts(reportNumber, i),
                releasingVolts = getReleasingVolts(reportNumber, i),
                brakeResistance = getBrakeResistance(reportNumber, i),
            )
        )

    }
    return reports
}

/**
 * Get resistance table.
 * The table includes columns: measured data, resistance standard, judge result.
 *
 * @param reportNumber - the number of the report from which you want to get data.
 * @param axesNumber - the axis on which you want to receive data.
 *
 * @return resistance table
 */
private suspend fun RXTX.getResistance(reportNumber: Int, axesNumber: Int): List<ResistanceLine> {
    val resistances = mutableListOf<ResistanceLine>()

    for (j in 0..14) {
        // Measure data
        var num = j.toHEXAndAddSymbol('0')
        val measureData = sendRequest(reportNumber, "2$num$axesNumber")

        // Resistance standards[Ohm]
        num = j.toHEXAndAddSymbol('1')
        val resistanceStandard = sendRequest(reportNumber, "2$num$axesNumber")

        // Judge result
        num = j.toHEXAndAddSymbol('2')
        val judgeResult = sendRequest(reportNumber, "2$num$axesNumber")
        resistances.add(
            ResistanceLine(
                measureData = measureData.codeToLongWithCheck()?.div(1000.0),
                resistanceStandard = if ((j < 12) || (axesNumber == 0)) resistanceStandard.codeToLongWithCheck()?.div(1000.0) else null,
                judgeResult = judgeResult.codeToJudgeResult()
            )
        )
    }

    return resistances
}

/**
 * Get attracting volts table.
 * The table includes columns: measured data, mean data, volt standard, judge result.
 *
 * @param reportNumber - the number of the report from which you want to get data.
 * @param axesNumber - the axis on which you want to receive data.
 *
 * @return attracting volts table
 */
private suspend fun RXTX.getAttractingVolts(reportNumber: Int, axesNumber: Int): AttractingVolts {
    // Measured data
    val measureData = mutableListOf<Double?>()
    for (j in 0..4) {
        val num = j.toHEXAndAddSymbol('4')
        measureData.add(sendRequest(reportNumber, "2$num$axesNumber").codeToIntWithCheck()?.div(1000.0))
    }

    return AttractingVolts(
        measureData = measureData,
        meanData = sendRequest(reportNumber, "24A$axesNumber").codeToIntWithCheck()?.div(1000.0),
        voltStandard = sendRequest(reportNumber, "24C$axesNumber").codeToIntWithCheck()?.div(1000.0),
        judgeResult = sendRequest(reportNumber, "24E$axesNumber").codeToJudgeResult(),
    )
}

/**
 * Get releasing volts table.
 * The table includes columns: measured data, mean data, volt standard, judge result.
 *
 * @param reportNumber - the number of the report from which you want to get data.
 * @param axesNumber - the axis on which you want to receive data.
 *
 * @return releasing volts table
 */
private suspend fun RXTX.getReleasingVolts(reportNumber: Int, axesNumber: Int): ReleasingVolts {
    val measureData = mutableListOf<Double?>()
    // Measured data
    for (j in 0..4) {
        val num = j.toHEXAndAddSymbol('5')
        measureData.add(sendRequest(reportNumber, "2$num$axesNumber").codeToIntWithCheck()?.div(1000.0))
    }

    return ReleasingVolts(
        measureData = measureData,
        meanData = sendRequest(reportNumber, "25A$axesNumber").codeToIntWithCheck()?.div(1000.0),
        voltStandard = sendRequest(reportNumber, "25C$axesNumber").codeToIntWithCheck()?.div(1000.0),
        judgeResult = sendRequest(reportNumber, "25E$axesNumber").codeToJudgeResult()
    )
}

/**
 * Get brake resistance table.
 * The table includes columns: measured data, mean data, volt standard, judge result.
 *
 * @param reportNumber - the number of the report from which you want to get data.
 * @param axesNumber - the axis on which you want to receive data.
 *
 * @return brake resistance table
 */
private suspend fun RXTX.getBrakeResistance(reportNumber: Int, axesNumber: Int): BrakeResistance {
    return BrakeResistance(
        measureData = sendRequest(reportNumber, "260$axesNumber").codeToLong().div(1000.0),
        voltStandard = sendRequest(reportNumber, "26C$axesNumber").codeToLong().div(1000.0),
        judgeResult = sendRequest(reportNumber, "26E$axesNumber").codeToJudgeResult()
    )
}

/**
 * Sending a request with a specific header and footer.
 *
 * @param reportNumber - the number of the report from which you want to get data.
 * @param requestBody - sending a request with a specific header and footer and waiting
 * for a response from the equipment.
 *
 * @return response from the equipment
 */
private suspend fun RXTX.sendRequest(reportNumber: Int, requestBody: String): String {
    val header = 0x05.toChar() + "00FFQR1R"
    val footer = "01${0x0d.toChar()}${0x0a.toChar()}"

    val request = header + reportNumber.toString().padStart(2, '0') + requestBody + footer
    return sendWithCallBack(request)
}

/**
 * Translation into the 16-matrix system and addition of the symbol.
 */
private fun Int.toHEXAndAddSymbol(addSymbol: Char): String = toString(16).uppercase().padStart(2, addSymbol)