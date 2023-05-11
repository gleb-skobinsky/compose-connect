package platform

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerHoverIcon

actual fun Modifier.userInputModifier(): Modifier = this

actual fun Modifier.statusBarsPaddingMpp(): Modifier = this

actual fun Modifier.pointerCursor() = this

actual fun Modifier.textCursor(): Modifier = this