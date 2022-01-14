package windows

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import strings.S

@Composable
fun RobotConnectionWindow (
    onCloseRequest: () -> Unit,
    onConnect: (ip: String, port: Int) -> Unit,
) {
    val ip = remember { mutableStateOf("localhost") }
    val port = remember { mutableStateOf("9105") }
    Window (
        onCloseRequest = onCloseRequest,
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        Column {
            Text(S.strings.enterIp)
            TextField(
                value = ip.value,
                onValueChange = {
                    ip.value = it
                }
            )

            Text(S.strings.enterPort)
            //TODO: Check int value
            TextField(
                value = port.value,
                onValueChange = {
                    port.value = it
                }
            )

            Button(
                onClick = {
                    onConnect(ip.value, port.value.toInt())
                }
            ) {
                Text(S.strings.connect)
            }
        }
    }
}