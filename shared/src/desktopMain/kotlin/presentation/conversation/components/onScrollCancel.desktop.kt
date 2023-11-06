package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
actual fun Modifier.onScrollCancel(pagerState: PagerState, action: () -> Unit): Modifier= composed {
    var currentEventCount by remember { mutableStateOf(0) }
    LaunchedEffect(currentEventCount) {
        if (currentEventCount != 0) {
            delay(100L)
            action()
        }
    }
    return@composed onPointerEvent(PointerEventType.Scroll, PointerEventPass.Initial) {
        currentEventCount += 1
    }
}