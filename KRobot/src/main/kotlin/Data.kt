import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val backup: List<String>,
    val robotType: String,
    val uptimeController: Double,
    val uptimeServo: Double,
    val brakeCounter: Int,
    val eStopCounter: Int,
    val motorOnCounter: Int,
    val motorsMoveTimes: List<Double?> = emptyList(),
    val motorsMoveAngles: List<Double?> = emptyList(),
)

suspend fun KRobot.getData(dataReadStatus: MutableStateFlow<String>? = null): Data {
    val backup = loadFileFromRobot(dataReadStatus)

    // OPEINFO - информация о роботе
    var robotType = ""
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

    val motorsMoveTime = mutableListOf<Double?>()
    val motorsMoveAngle = mutableListOf<Double?>()

    coroutineScope.launch {
        backup.forEach {
            with(it) {
                when {
                    contains("OPEINFO  ") -> {
                        println(it)
                        println(it.substringAfter(")").trim())
                        robotType = it.substringAfter(")").trim()
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
        uptimeController,
        uptimeServo,
        brakeCounter,
        eStopCounter,
        motorOnCounter,
        motorsMoveTime,
        motorsMoveAngle
    )
    return data
}

private fun String.getValue(): String = split("  ")[1].trim()


