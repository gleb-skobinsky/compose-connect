package platform

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.browser.document

actual fun Modifier.userInputModifier(): Modifier = this

actual fun Modifier.statusBarsPaddingMpp(): Modifier = this

fun Modifier.jsCursor(cursor: String) = composed {
    val hovered = remember { mutableStateOf(false) }

    if (hovered.value) {
        document.body?.style?.cursor = cursor
    } else {
        document.body?.style?.cursor = "default"
    }

    this.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val pass = PointerEventPass.Main
                val event = awaitPointerEvent(pass)
                val isOutsideRelease = event.type == PointerEventType.Release &&
                        event.changes[0].isOutOfBounds(size, Size.Zero)
                hovered.value = event.type != PointerEventType.Exit && !isOutsideRelease
            }
        }
    }
}


actual fun Modifier.pointerCursor(): Modifier = jsCursor("pointer")

actual fun Modifier.textCursor(): Modifier = jsCursor("text")