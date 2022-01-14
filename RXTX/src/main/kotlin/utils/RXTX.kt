package utils

import const.connectSuccess
import gnu.io.NRSerialPort
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import java.io.*

/**
 * The class is responsible for communication with various equipment via the COM port.
 *
 */
class RXTX() {
    private var _isConnect = MutableStateFlow(false)
    var isConnect: StateFlow<Boolean> = _isConnect

    private lateinit var out: DataOutputStream
    private lateinit var serial: NRSerialPort

    private val _buffer = MutableStateFlow<String?>(null)

    private lateinit var coroutineScope: CoroutineScope

    /**
     * Getting COM ports allowed for connection.
     *
     * @return list of COM ports allowed for connection.
     */
    fun getAvailableSerialPorts(): List<String> {
        val availableSerialPortsList = mutableListOf<String>()
        for (s in NRSerialPort.getAvailableSerialPorts()) {
            availableSerialPortsList.add(s)
        }
        return availableSerialPortsList
    }

    /**
     * Connecting to the device.
     */
    suspend fun connect(portName: String, coroutineScope: CoroutineScope) {
        println("Connect")
        this.coroutineScope = coroutineScope
        try {
            serial = NRSerialPort(portName, 115200)

            serial.connect()
            _isConnect.value = true

            val reader = BufferedReader(InputStreamReader(serial.inputStream))
            out = DataOutputStream(serial.outputStream)

            // Start handling incoming text
            try {
                coroutineScope.launch {
                    startReading(reader)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            sendWithCallBack(connectSuccess)
        } catch (ex: Exception) {
            _isConnect.value = false
            ex.printStackTrace()
        }

    }

    /**
     * Send text message to the device.
     *
     * @param message - text message.
     */
    fun send(message: String) {
        out.writeBytes(message)
        out.flush()
    }


    /**
     * Send bytes array to the device.
     *
     * @param bytes - bytes array.
     */
    fun send(bytes: ByteArray) {
        out.write(bytes)
        out.flush()
    }

    suspend fun sendWithCallBack(bytes: ByteArray): String {
        _buffer.value = null
        send(bytes)
        coroutineScope.launch {
            _buffer.collect {
                if (it != null) this.cancel()
            }
        }.join()
        val line = _buffer.value
        _buffer.value = null
        return line!!
    }

    suspend fun sendWithCallBack(message: String): String {
        _buffer.value = null
        send(message)
        coroutineScope.launch {
            _buffer.collect {
                if (it != null) this.cancel()
            }
        }.join()
        val line = _buffer.value
        _buffer.value = null
        return line!!
    }

    /**
     * Disabling the device.
     */
    fun disconnect() {
        _isConnect.value = false
        serial.disconnect()
    }

    private suspend fun startReading(reader: BufferedReader) {
        while (_isConnect.value) {
            if (reader.ready()) {
                val b = reader.readLine()
                _buffer.value = b
            }

            delay(30L)
        }
    }
}