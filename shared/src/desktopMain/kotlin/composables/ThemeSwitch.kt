package composables

import androidx.compose.runtime.Composable
import data.AdditionalUiState

@Composable
actual fun ThemeSwitch(
    uiState: AdditionalUiState,
    onThemeChange: (Boolean) -> Unit
) {
    CommonThemeSwitch(uiState, onThemeChange)
}