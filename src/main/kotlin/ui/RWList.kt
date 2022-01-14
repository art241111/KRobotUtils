package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Custom list for Robowizard company
 */
@Composable
fun RWList(
    modifier: Modifier = Modifier.fillMaxHeight(),
    items: List<String>,
    onSelect: (index: Int) -> Unit,
    buttonText: String
) {
    val selectIndex = remember { mutableStateOf(-1) }

    Box(modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            itemsIndexed(items) { index, item ->
                ListItem(
                    isSelect = selectIndex.value == index,
                    onClick = {
                        selectIndex.value = index
                    },
                    text = item,
                    onDoubleClick = { onSelect(selectIndex.value) }
                )
            }

            item {
                Button(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(15.dp),
                    onClick = { onSelect(selectIndex.value) }
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}