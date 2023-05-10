package composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import data.MainViewModel
import data.AppScreenState
import data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.statusBarsPaddingMpp
import platform.userInputModifier
import themes.toTheme
import transport.getTimeNow

@Composable
@Suppress("FunctionName")
fun Conversation(
    conversationUiState: MainViewModel,
    scrollState: LazyListState,
    uiState: MainViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val drawerOpen by uiState.drawerShouldBeOpened.collectAsState()
    val screenState by uiState.screenState.collectAsState()
    val currentUser by uiState.selectedUserProfile.collectAsState()
    if (drawerOpen) {
        // Open drawer and reset state in VM.
        LaunchedEffect(Unit) {
            scaffoldState.drawerState.open()
            uiState.resetOpenDrawerAction()
        }
    }

    JetchatScaffold(
        scaffoldState = scaffoldState,
        uiState = uiState,
        onChatClicked = { title ->
            uiState.setCurrentConversation(title)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onProfileClicked = { userId ->
            uiState.setCurrentAccount(userId)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onThemeChange = { value ->
            uiState.switchTheme(value.toTheme())
        }
    ) {
        when (screenState) {
            AppScreenState.CHAT -> ConversationContent(
                conversationUiState = conversationUiState,
                scrollState = scrollState,
                scope = coroutineScope
            ) {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }

            AppScreenState.ACCOUNT -> ProfileScreen(currentUser!!) {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
private fun ConversationContent(
    conversationUiState: MainViewModel,
    scrollState: LazyListState,
    scope: CoroutineScope,
    onNavIconPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val messagesState by conversationUiState.conversationUiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Messages(messagesState, scrollState)
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            UserInput(
                onMessageSent = { content ->
                    val timeNow = getTimeNow()
                    val message = Message("me", content, timeNow)
                    conversationUiState.sendMessage(message)
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
            channelName = messagesState.channelName,
            channelMembers = messagesState.channelMembers,
            onNavIconPressed = onNavIconPressed,
            scrollBehavior = scrollBehavior,
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPaddingMpp(),
        )
    }
}