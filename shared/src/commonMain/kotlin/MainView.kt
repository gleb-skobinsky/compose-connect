import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import composables.Conversation
import data.AdditionalUiState
import data.exampleUiState
import kotlinx.coroutines.CoroutineScope
import themes.JetchatTheme
import transport.getPlatformWebsocket

@Composable
@Suppress("FunctionName")
fun MainView() {
    val uiState = AdditionalUiState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val ws: Any? = remember { getPlatformWebsocket() }
    ThemeWrapper(uiState, coroutineScope, scrollState, ws)
}

@Composable
fun ThemeWrapper(
    uiState: AdditionalUiState,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    ws: Any?,
) {
    val theme by uiState.themeMode.collectAsState()
    JetchatTheme(theme) {
        Column {
            Conversation(
                conversationUiState = exampleUiState,
                scope = coroutineScope,
                scrollState = scrollState,
                webSocket = ws,
                uiState = uiState
            )
        }
    }
}