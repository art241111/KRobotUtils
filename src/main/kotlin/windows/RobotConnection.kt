package windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableStateFlow
import strings.S

@Composable
fun RobotConnectionWindow(
    onClose: () -> Unit,
    onConnect: (ip: String, port: Int) -> Unit,
    dataReadStatus: MutableStateFlow<String>,
) {
    val ip = remember { mutableStateOf("localhost") }
    val port = remember { mutableStateOf("9105") }
    Window(
        title = S.strings.robotConnection,
        onCloseRequest = onClose,
        state = rememberWindowState(width = 300.dp, height = 280.dp)
    ) {
        Box () {
            Column (
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(10.dp))
                Text(S.strings.robotConnection)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = ip.value,
                    onValueChange = {
                        ip.value = it
                    },
                    label = {
                        Text(S.strings.enterIp)
                    }
                )
                Spacer(Modifier.height(10.dp))
                //TODO: Check int value
                OutlinedTextField(
                    value = port.value,
                    onValueChange = {
                        port.value = it
                    },
                    label = {
                        Text(S.strings.enterPort)
                    }
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {
                        onConnect(ip.value, port.value.toInt())

                    }
                ) {
                    Text(S.strings.connect)
                }
            }

            val dataReadStatusComments = dataReadStatus.collectAsState()
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = dataReadStatusComments.value
            )
        }
    }
}