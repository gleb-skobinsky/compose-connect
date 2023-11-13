package presentation.conversation.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class ScrollDirection {
    BACK,
    FORWARD
}

private var scrollJob: Job? = null
private const val MAX_VELOCITY = 50f

private var totalScrollAmount = 0f

fun Modifier.desktopSnapFling(
    listState: LazyListState,
    localScrollScope: CoroutineScope,
    onSlide: suspend (ScrollDirection) -> Unit
) = pointerInput(Unit) {
    awaitEachGesture {
        val event = awaitPointerEvent(PointerEventPass.Initial)
        val delta = event.changes[0].scrollDelta
        if (event.type == PointerEventType.Scroll) {
            if (abs(delta.x) > abs(delta.y)) {
                if (!listState.isScrollInProgress && scrollJob?.isActive != true) {
                    totalScrollAmount += delta.x
                    if (totalScrollAmount > MAX_VELOCITY) {
                        scrollJob = localScrollScope.launch {
                            onSlide(ScrollDirection.FORWARD)
                            totalScrollAmount = 0f
                        }
                    } else if (totalScrollAmount < -MAX_VELOCITY) {
                        scrollJob = localScrollScope.launch {
                            onSlide(ScrollDirection.BACK)
                            totalScrollAmount = 0f
                        }
                    } else {
                        event.changes.forEach { it.consume() }
                    }
                } else {
                    event.changes.forEach { it.consume() }
                }
            }
        }
    }
}
    .onScrollCancel {
        totalScrollAmount = 0f
    }
