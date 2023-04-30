package composables

import androidx.compose.runtime.Composable
import data.AdditionalUiState

@Composable
expect fun ThemeSwitch(uiState: AdditionalUiState, onThemeChange: (Boolean) -> Unit)