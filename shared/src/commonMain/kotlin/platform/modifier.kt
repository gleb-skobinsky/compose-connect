package platform

import androidx.compose.ui.Modifier

expect fun Modifier.userInputModifier(): Modifier

expect fun Modifier.statusBarsPaddingMpp(): Modifier