package presentation.conversation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Stable
enum class ImageNavigation {
    PREV,
    NEXT
}

@Composable
fun ImageNavigate(
    type: ImageNavigation,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(100.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (type) {
            ImageNavigation.PREV -> {
                Icon(Icons.Outlined.ArrowBack, null, tint = Color.White)
            }

            ImageNavigation.NEXT -> {
                Icon(Icons.Outlined.ArrowForward, null, tint = Color.White)
            }
        }
    }
}