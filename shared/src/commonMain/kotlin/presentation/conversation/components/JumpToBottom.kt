package presentation.conversation.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BoxScope.JumpToBottom(
    scrollState: LazyListState,
    enabled: Boolean
) {
    val scope = rememberCoroutineScope()
    val transition = updateTransition(
        enabled,
        label = "JumpToBottom visibility animation"
    )
    val bottomOffset by transition.animateDp(label = "JumpToBottom offset animation") {
        if (it) {
            32.dp
        } else {
            (-32).dp
        }
    }
    if (bottomOffset > 0.dp) {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDownward,
                    modifier = Modifier.height(18.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            },
            text = {
                Text(
                    text = "Jump to bottom",
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 0.dp, y = -bottomOffset)
                .height(36.dp)
        )
    }
}

