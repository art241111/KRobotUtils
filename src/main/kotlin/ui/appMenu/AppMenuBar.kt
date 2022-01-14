package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import strings.S

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppMenuBar(
    onSave: () -> Unit,
    onRobotConnect: () -> Unit,
    onBreakCheckerConnect: () -> Unit,
    breakCheckerStatus: String,
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
            text = "BreakCheckerText: $breakCheckerStatus",
            onClick = onBreakCheckerConnect
        )
    }
}