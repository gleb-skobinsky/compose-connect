package composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import data.AppScreenState
import data.MainViewModel
import data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.statusBarsPaddingMpp
import platform.userInputModifier
import transport.getTimeNow

@Composable
fun Conversation(
    viewModel: MainViewModel,
) {
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val drawerOpen by viewModel.drawerShouldBeOpened.collectAsState()
    val screenState by viewModel.screenState.collectAsState()
    val currentUser by viewModel.selectedUserProfile.collectAsState()
    if (drawerOpen) {
        // Open drawer and reset state in VM.
        LaunchedEffect(Unit) {
            scaffoldState.drawerState.open()
            viewModel.resetOpenDrawerAction()
        }
    }

    AppScaffold(
        scaffoldState = scaffoldState,
        viewModel = viewModel,
        onChatClicked = { title ->
            viewModel.setCurrentConversation(title)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onProfileClicked = { userId ->
            viewModel.setCurrentAccount(userId)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    ) {
        when (screenState) {
            AppScreenState.CHAT -> ConversationContent(
                viewModel = viewModel,
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
private fun ConversationContent(
    viewModel: MainViewModel,
    scrollState: LazyListState,
    scope: CoroutineScope,
    onNavIconPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val messagesState by viewModel.conversationUiState.collectAsState()
    val user by viewModel.user.collectAsState()
    RoomCreationDialog(viewModel)
    Box(modifier = Modifier.fillMaxSize()) {
        Messages(messagesState, user, scrollState)
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            UserInput(
                onMessageSent = { content ->
                    val timeNow = getTimeNow()
                    viewModel.user.value?.let { user ->
                        val message = Message(author = user.email, content = content, timestamp = timeNow)
                        viewModel.sendMessage(message)
                    }
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