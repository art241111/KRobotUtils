import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val backup: List<String>,
    val robotType: String,
    val serialNumber: String = "",
    val axesCount: Int = 0,
    val uptimeController: Double,
    val uptimeServo: Double,
    val brakeCounter: Int,
    val eStopCounter: Int,
    val motorOnCounter: Int,
    val motorsMoveTimes: List<Double?> = emptyList(),
    val motorsMoveAngles: List<Double?> = emptyList(),
)

suspend fun KRobot.getDataFromBackup(
    backup: List<String>
) {
    // OPEINFO - информация о роботе
    var robotType = ""
    var serialNumber = ""
    var axesCount = 0
    // CONT_TIM - часы наработки контроллера
    var uptimeController = 0.0
    // SERV_TIM - время работы сево-моторов
    var uptimeServo = 0.0
    // BRKE_CNT - brake counter
    var brakeCounter = 0
    // ESTP_CNT - кличество e-stop
    var eStopCounter = 0
    // MTON_CNT - motor on counter
    var motorOnCounter = 0

    // M_MOVE_TJT
    val motorsMoveTime = mutableListOf<Double?>()
    // M_DIST_DJT
    val motorsMoveAngle = mutableListOf<Double?>()

    coroutineScope.launch {
        backup.forEach {
            with(it) {
                when {
                    contains("ZROBOT.TYPE") -> {
                        val str = this.split(" ").filter { it != "" }
                        robotType = str[6]
                        axesCount = str[3].toIntOrNull() ?: 0
                    }
                    contains("OPEINFO  ") -> {
                        val split = this.split("  ")
                        serialNumber = split[1].split(" ").last()
                    }
                    contains("CONT_TIM") -> uptimeController = getValue().toDouble()
                    contains("SERV_TIM") -> uptimeServo = getValue().toDouble()
                    contains("BRKE_CNT") -> brakeCounter = getValue().toInt()
                    contains("ESTP_CNT") -> eStopCounter = getValue().toInt()
                    contains("MTON_CNT") -> motorOnCounter = getValue().toInt()
                    contains("M_MOVE_TJT") -> {
                        motorsMoveTime.addAll(getValue().split(" ").map { value ->
                            value.trim().toDoubleOrNull()
                        })
                    }
                    contains("M_DIST_DJT") -> {
                        motorsMoveAngle.addAll(getValue().split(" ").map { value ->
                            value.trim().toDoubleOrNull()
                        })
                    }

                }
            }
        }
    }.join()
    val data = Data(
        backup,
        robotType,
        serialNumber,
        axesCount,
        uptimeController,
        uptimeServo,
        brakeCounter,
        eStopCounter,
        motorOnCounter,
        motorsMoveTime,
        motorsMoveAngle
    )
    this.data = data
}

suspend fun KRobot.getData(dataReadStatus: MutableStateFlow<String>? = null) {
    val backup = loadFileFromRobot(dataReadStatus)
    getDataFromBackup(backup)
}

private fun String.getValue(): String = split("  ")[1].trim()


