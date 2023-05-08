import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import composables.Conversation
import data.AdditionalUiState
import themes.JetchatTheme

@Composable
@Suppress("FunctionName")
fun Application() {
    val uiState = AdditionalUiState()
    val scrollState = rememberLazyListState()
    ThemeWrapper(uiState, scrollState)
}

@Composable
@Suppress("FunctionName")
fun ThemeWrapper(
    uiState: AdditionalUiState,
    scrollState: LazyListState,
) {
    val theme by uiState.themeMode.collectAsState()
    JetchatTheme(theme) {
        Column {
            Conversation(
                conversationUiState = uiState,
                scrollState = scrollState,
                uiState = uiState
            )
        }
    }
}