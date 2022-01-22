package ui.listOfData

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp

@Composable
fun ListOfData(
    modifier: Modifier,
    names: List<String>,
    onCheck: (index: Int) -> Unit,
    onCompare: (indexes: List<Int>) -> Unit,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
) {
    val checkIndexes = remember { mutableStateListOf<Int>() }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column {
            // Buttons
            ListOfDataButtons(
                onAdd = onAdd,
                onDelete = onDelete,
                onCompare = { onCompare(checkIndexes) }
            )
            names.forEachIndexed { index, names ->
                ListOfDataItems(
                    modifier = Modifier
//                        .onPreviewKeyEvent {
//                        when{
//                            it.isShiftPressed && it.type == KeyEventType.KeyDown
//                        }
//                    }
                        .clickable {
                        onCheck(index)
                    },
                    text = names
                )
            }
        }
    }
}