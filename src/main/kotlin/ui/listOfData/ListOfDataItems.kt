package ui.listOfData

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ListOfDataItems(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box {
        Text(text)
    }
}