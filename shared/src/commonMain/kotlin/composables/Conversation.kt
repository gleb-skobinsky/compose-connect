package composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import data.AdditionalUiState
import data.ConversationUiState
import data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.statusBarsPaddingMpp
import platform.userInputModifier
import themes.toTheme
import transport.getTimeNow
import transport.onMessageEnter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conversation(
    conversationUiState: ConversationUiState,
    scope: CoroutineScope,
    scrollState: LazyListState,
    webSocket: Any?,
    uiState: AdditionalUiState,
) {
    val scaffoldState = rememberScaffoldState()
    val drawerOpen by uiState.drawerShouldBeOpened.collectAsState()
    if (drawerOpen) {
        // Open drawer and reset state in VM.
        LaunchedEffect(Unit) {
            scaffoldState.drawerState.open()
            uiState.resetOpenDrawerAction()
        }
    }

    JetchatScaffold(
        scaffoldState,
        uiState,
        onChatClicked = {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onProfileClicked = {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onThemeChange = { value ->
            uiState.switchTheme(value.toTheme())
        }
    ) {
        ConversationContent(
            conversationUiState = conversationUiState,
            scrollState = scrollState,
            webSocket = webSocket,
            scope = scope,
            onNavIconPressed = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationContent(
    conversationUiState: ConversationUiState,
    scrollState: LazyListState,
    webSocket: Any?,
    scope: CoroutineScope,
    onNavIconPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Box(modifier = Modifier.fillMaxSize()) {
        Messages(conversationUiState, scrollState)
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            UserInput(
                onMessageSent = { content ->
                    val timeNow = getTimeNow()
                    val message = Message("me", content, timeNow)
                    webSocket?.let { onMessageEnter(message, it) }
                    conversationUiState.addMessage(message)
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                // Use navigationBarsWithImePadding(), to move the input panel above both the
                // navigation bar, and on-screen keyboard (IME)
                modifier = Modifier.userInputModifier(),
            )
        }
        ChannelNameBar(
            channelName = conversationUiState.channelName,
            channelMembers = conversationUiState.channelMembers,
            onNavIconPressed = onNavIconPressed,
            scrollBehavior = scrollBehavior,
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPaddingMpp(),
        )
    }
}