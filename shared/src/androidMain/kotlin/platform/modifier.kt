package platform

import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

actual fun Modifier.userInputModifier(): Modifier = this.navigationBarsWithImePadding()

actual fun Modifier.statusBarsPaddingMpp(): Modifier = this.statusBarsPadding()

actual fun Modifier.pointerCursor() = this

actual fun Modifier.textCursor(): Modifier = this