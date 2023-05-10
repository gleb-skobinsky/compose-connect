package composables

import androidx.compose.runtime.Composable
import data.MainViewModel

@Composable
actual fun ThemeSwitch(
    uiState: MainViewModel,
    onThemeChange: (Boolean) -> Unit
) {
    CommonThemeSwitch(uiState, onThemeChange)
}