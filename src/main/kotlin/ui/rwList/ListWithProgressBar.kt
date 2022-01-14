package ui.rwList

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import ui.RWList

@Composable
fun ListWithProgressBar(
    list: List<String>,
    buttonText: String,
    onSelect: (index: Int) -> Unit
) {
    if (list.isEmpty()) {
        CircularProgressIndicator()
    }

    RWList(
        items = list,
        onSelect = { index ->
            onSelect(index)
        },
        buttonText = buttonText
    )
}