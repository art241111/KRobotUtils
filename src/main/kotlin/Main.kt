// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import strings.S
import windows.RobotConnectionWindow

fun main() = application {
    var isRobotConnecting by remember { mutableStateOf(false) }
    var isBreakCheckerConnecting by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val robot = remember { KRobot(coroutineScope) }

    Window(
        onCloseRequest = ::exitApplication,
        title = S.strings.title,
        state = rememberWindowState(width = 900.dp, height = 600.dp)
    ) {

        MaterialTheme {
            Column {
                AppMenuBar(
                    onSave = {},
                    onRobotConnect = {
                        if (isRobotConnecting) {
                            robot.disconnect()
                        } else {
                            isRobotConnecting = true
                        }
                    },
                    onBreakCheckerConnect = { isBreakCheckerConnecting = true },
                )
            }
        }
    }

    when {
        isRobotConnecting -> {
            RobotConnectionWindow(
                onCloseRequest = { isRobotConnecting = false },
                onConnect = { ip, port ->
                    robot.connect(ip, port)
                }
            )
        }

        isBreakCheckerConnecting -> {
            // TODO: Break checker connection window
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMenuBar(
    onSave: () -> Unit,
    onRobotConnect: () -> Unit,
    onBreakCheckerConnect: () -> Unit,
) {
    Row {
        MenuBarItem(
            text = S.strings.save,
            onClick = onSave
        )

        MenuBarItem(
            text = S.strings.toRobot,
            onClick = onRobotConnect
        )

        MenuBarItem(
            text = S.strings.toBreakChecker,
            onClick = onBreakCheckerConnect
        )
    }
}

/**
 *
 * Without the content parameter, the menu becomes a simple button.
 */
@Composable
private fun MenuBarItem(
    text: String = "",
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = {
            onClick()
            expanded = !expanded
        }) {
            Text(text)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            }
        ) {
            content()
        }
    }


}