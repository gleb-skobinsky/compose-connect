package presentation.conversation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

/*
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.closeOnDrag(maxHeight: Dp, onClose: () -> Unit): Modifier =
    composed {
        val scope = rememberCoroutineScope()
        var offset by remember { mutableStateOf(0f) }
        val quarter = maxHeight / 4
        var alpha by remember { mutableStateOf(1f) }
        with(LocalDensity.current) {
            return@composed offset(y = offset.dp)
                .alpha(alpha)
                .onDrag(
                    onDragCancel = {
                        val closed = offset !in -quarter.value..quarter.value
                        if (closed) {
                            onClose()
                        } else {
                            scope.launch {
                                animate(offset, 0f) { value, _ ->
                                    offset = value
                                    alpha = quarter.value / abs(offset) / 3f
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        val closed = offset !in -quarter.value..quarter.value
                        if (closed) {
                            onClose()
                        } else {
                            scope.launch {
                                animate(offset, 0f) { value, _ ->
                                    offset = value
                                    alpha = quarter.value / abs(offset) / 3f
                                }
                            }
                        }
                    }
                ) {
                    offset += it.y.toDp().value
                    alpha = quarter.value / abs(offset) / 3f
                }
        }
    }
 */

fun Modifier.closeOnScroll(
    maxHeight: Dp,
    onClose: () -> Unit
) = composed {
    with(LocalDensity.current) {
        val quarter = maxHeight / 4
        var offset by remember { mutableStateOf(0.dp) }
        var alpha by remember { mutableStateOf(1f) }
        val scrollable = rememberScrollableState {
            offset += it.dp
            alpha = quarter.value / abs(offset.value) / 3f
            offset.value
        }
        LaunchedEffect(offset.value) {
            snapshotFlow { offset }.collectLatest {
                delay(50)
                if (offset !in -quarter..quarter) {
                    onClose()
                } else {
                    scrollable.animateScrollBy(
                        value = -offset.value,
                        animationSpec = tween(1000)
                    )
                }
            }
        }
        return@composed scrollable(scrollable, Orientation.Vertical)
            .alpha(alpha)
            .offset(y = offset)
    }
}