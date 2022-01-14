package windows

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.ListItem
import utils.RXTX
import utils.rxtxExtensions.getReportNames

@Composable
fun ConnectToBreakChecker(
    listAllowedPorts: MutableState<List<String>>,
    coroutineScope: CoroutineScope,
    rxtx: RXTX,
    reportNames: MutableState<List<String>>,
    onClose: () -> Unit
) {
    Window(
        onCloseRequest = { onClose() },
        state = rememberWindowState(width = 300.dp, height = 400.dp),
        resizable = false,
        title = "Connect to break checker"
    ) {
        val selectIndex = remember { mutableStateOf(0) }
        val stateVertical = rememberScrollState(0)

        val onSelect = {
            coroutineScope.launch {
                rxtx.connect(listAllowedPorts.value[selectIndex.value], coroutineScope)
                val _report = rxtx.getReportNames()
                reportNames.value = _report
                onClose()
            }
        }
        Box (modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Список допустимых COM портов",
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                if (listAllowedPorts.value.isEmpty()) {
                    CircularProgressIndicator()
                }
                LazyColumn(contentPadding = PaddingValues(vertical = 10.dp)) {
                    itemsIndexed(listAllowedPorts.value) { index, item ->
                        ListItem(
                            isSelect = selectIndex.value == index,
                            onClick = {
                                selectIndex.value = index
                            },
                            text = item,
                            onDoubleClick = { onSelect() }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Button(onClick = { onSelect() }) {
                    Text("Connect")
                }

                Spacer(Modifier.height(10.dp))
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(stateVertical)
            )
        }

    }
}