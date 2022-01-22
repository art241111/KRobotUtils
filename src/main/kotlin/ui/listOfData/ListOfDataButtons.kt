package ui.listOfData

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.CssGgIcons
import compose.icons.cssggicons.Add
import compose.icons.cssggicons.MathMinus
import compose.icons.cssggicons.PathExclude

@Composable
fun ListOfDataButtons(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
    onCompare: () -> Unit,
) {
    Row(modifier) {
        IconButton(
            onClick = onCompare
        ) {
            Icon(CssGgIcons.PathExclude, "Compare")
        }

        IconButton(
            onClick = onDelete
        ) {
            Icon(CssGgIcons.MathMinus, "-")
        }

        IconButton(
            onClick = onAdd
        ) {
            Icon(CssGgIcons.Add, "+")
        }
    }
}

@Preview
fun ListOfDataButtonsPreview() {
    ListOfDataButtons(
        onAdd = {},
        onDelete = {},
        onCompare = {}
    )
}