package composables

import androidx.compose.runtime.Composable
import data.MainViewModel

@Composable
expect fun ThemeSwitch(uiState: MainViewModel, onThemeChange: (Boolean) -> Unit)