package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun Table(
    col: Int,
    row: Int,
    content: @Composable (col: Int, row: Int) -> Unit
) {

    Column {
        for (i in 0 until row) {
            Row {
                for (j in 0 until col) {
                    content(i , j)
                }
            }
        }
    }

}