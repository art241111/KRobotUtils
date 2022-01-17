package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun Table(
    col: Int,
    count: Int,
    content: @Composable (index: Int) -> Unit
) {
    val rowCount = count / 2
    val row = if (rowCount % 2 == 0) rowCount else rowCount + 1

    Column {
        for (i in 0 until row) {
            Row {
                for (j in 0 until col) {
                    val index = i * col + j

                    if ((index + 1) <= count) {
                        content(index)
                    }
                }
            }
        }
    }
}