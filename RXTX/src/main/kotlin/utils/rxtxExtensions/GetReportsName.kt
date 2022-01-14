package utils.rxtxExtensions

import kotlinx.coroutines.CoroutineScope
import utils.RXTX
import utils.codeToStr
import utils.toThreeNumbers

/**
 * Get report names from Break Checker.
 *
 * @return list of report names
 *
 * TODO: Get data create
 */
suspend fun RXTX.getReportNames(): List<String> {
    val reportNames = mutableListOf<String>()

    val header = 0x05.toChar() + "00FFQR1R"
    val footer = "${0x0d.toChar()}${0x0a.toChar()}"
    val getName = "9700A"

    for (i in 1..50) {
        //serialConnector.send("${header}0${i}098007")
        val request = "$header${(i * 10).toThreeNumbers()}$getName$footer"
        reportNames.add(sendWithCallBack(request).codeToStr())
    }

    return reportNames
}