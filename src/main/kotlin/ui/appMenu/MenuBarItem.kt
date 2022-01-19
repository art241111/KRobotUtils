package ui.appMenu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *
 * Without the content parameter, the menu becomes a simple button.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuBarItem(
    text: String = "",
    onClick: () -> Unit = {},
) {
    Surface(
        onClick = { onClick() },
        shape = RoundedCornerShape(5.dp),
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth()

    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 5.dp),
            text = text,
            color = MaterialTheme.colors.secondary,
            fontSize = 15.sp,
        )
    }
}