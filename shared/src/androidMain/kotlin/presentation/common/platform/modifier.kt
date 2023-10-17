package presentation.common.platform

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

actual fun Modifier.userInputModifier(): Modifier = this.navigationBarsWithImePadding()

actual fun Modifier.statusBarsPaddingMpp(): Modifier = this.statusBarsPadding()

actual fun Modifier.pointerCursor() = this

actual fun Modifier.textCursor(): Modifier = this