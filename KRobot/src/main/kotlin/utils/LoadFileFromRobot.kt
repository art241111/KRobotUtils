import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.io.File

// Request to read the program
private val START_LOAD = charArrayOf(
    0x02.toChar(),
    0x42.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x30.toChar(),
    0x17.toChar()
)

// The sequence of bytes that is responsible for the fact that the program was read
private val STOP_LOAD = charArrayOf(
    0x02.toChar(),
    0x45.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x20.toChar(),
    0x30.toChar(),
    0x17.toChar()
)

/**
 * Uploading a backup of the robot.
 *
 * @return List of lines with the robot program.
 */
suspend fun KRobot.loadFileFromRobot(dataReadStatus: MutableStateFlow<String>? = null): List<String> {
    val response = mutableListOf<String>()
    // Sending save command
    send("save using.rcc")

    // Sending start sequence
    send(START_LOAD)

    // Reading the program
    val localJob = coroutineScope.launch (Dispatchers.IO) {
        var line = ""

        incomingLetter.collect{ letter ->
            // Checking end line
            if (letter != "\n") {
                line += letter
            } else {
                // If the text starts with 0x05 0x02 0x44 bytes then it belongs to the program, and it needs to be saved
                val programPrefix = charArrayOf(0x05.toChar(), 0x02.toChar(),0x44.toChar()).joinToString("")
                if (line.contains(programPrefix) ) {
                    response.add(line.substringAfter(programPrefix))
                } else {
                    // Comment line
                    println(line)

                    if (dataReadStatus != null) dataReadStatus.value = line
                }

                line = ""
            }

            // Checking the end of the file
            if (line.contains(charArrayOf(0x05.toChar(), 0x02.toChar(), 0x45.toChar()).joinToString(""))) {
                cancel()
            }
        }
    }

    // Waiting for a file to be read
    localJob.join()

    // Sending end sequence
    send(STOP_LOAD)

    // Clean return line
    if (dataReadStatus != null) dataReadStatus.value = ""

    // Delete first string
    // (Busing.rcc Saving...(using.rcc))
    response.removeAt(0)

    return response
    // CONT_TIM - часы наработки контроллера
    // SERV_TIM - время работы сево-моторов
    // OPEINFO - информация о роботе
    // ESTP_CNT - кличество e-stop
    // MTON_CNT - motor on counter
    // BRKE_CNT - brake counter
    // LAN_HOST_IPAD - ip address 1

}