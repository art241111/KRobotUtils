package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelect: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit = {}
) {
    val selectedColor = Color.LightGray
    val notSelectedColor = Color.White
    Card(
        modifier = modifier
            .pointerInput(Unit) {
                this.detectTapGestures(
                    onPress = {
                        onClick()
                    },
                    onDoubleTap = {
                        onDoubleClick()
                    }
                )
            }
            .fillMaxWidth(),
        backgroundColor = if (isSelect) selectedColor else notSelectedColor,
//        border = BorderStroke(1.dp, Color.Gray),
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = text
        )
    }
}