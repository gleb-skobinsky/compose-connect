package platform

import androidx.compose.ui.Modifier

expect fun Modifier.userInputModifier(): Modifier

expect fun Modifier.statusBarsPaddingMpp(): Modifier

expect fun Modifier.pointerCursor(): Modifier

expect fun Modifier.textCursor(): Modifier