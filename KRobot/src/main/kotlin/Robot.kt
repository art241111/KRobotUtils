import com.github.art241111.tcpClient.Client
import com.github.art241111.tcpClient.connection.Status
import com.github.poluka.kControlLibrary.Command
import com.github.poluka.kControlLibrary.move.JMove
import com.github.poluka.kControlLibrary.points.Point
import com.github.poluka.kControlLibrary.points.Where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KRobot(
    internal val coroutineScope: CoroutineScope,
    var ip: String = "192.168.0.2",
    var port: Int = 9105,
    var delaySending: Int = 0,
    var homePoint: Point = Point(),
) : Client() {
    private val _isConnect: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _position: MutableStateFlow<Point> = MutableStateFlow(Point())
    val position: StateFlow<Point> = _position

    var data: Data? = null
        private set

    /**
     * Update position
     */
    fun updatePosition() {
        send(Where())
    }

    val isConnect: StateFlow<Boolean> = _isConnect
    fun connect(ip: String, port: Int) {
        this.ip = ip
        this.port = port

        connect()
    }

    fun connect() {
        println("Connect")
        if (port != 0 && ip != "") {
            // Connect to the robot
            coroutineScope.launch {
                connect(
                    ip,
                    port,
                    defaultMessage = "as"
                )

                data = getData()
            }

            setConnectStatusHandler()
            setPositionHandler()
        }
    }

    fun disconnect() {
        disconnect(stopSymbol = "q")
    }

    fun send(command: Command) {
        send(command.generateCommand())
    }

    fun moveHome() {
        if (homePoint != Point()) {
            send(JMove(homePoint))
        }
    }


    private fun setConnectStatusHandler() {
        coroutineScope.launch(Dispatchers.IO) {
            statusState.collect {
                _isConnect.value = it == Status.COMPLETED
            }
        }
    }


    private fun setPositionHandler() {
        coroutineScope.launch(Dispatchers.IO) {
            var isNextPosition = false
            incomingText.collect { message ->
                // Это условие связано с тем, что робот пересылает сообщение с символом '>' или без символа
                // TODO: Обработчик полученных сообщений
                if (isNextPosition) {
                    val point = Point()
                    val split = message.split(" ")
                    var index = 0

                    split.forEach {
                        if (it.isNotEmpty() || it != "") {
                            point[index] = it.trim().toDouble()
                            index++
                        }
                    }
                    _position.value = point
                }
                isNextPosition = message.contains("X[mm]")
//                        _position.value = Point()
            }
        }
    }
}