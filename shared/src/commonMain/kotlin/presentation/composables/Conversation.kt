package presentation.composables

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
import presentation.platform.statusBarsPaddingMpp
import presentation.platform.userInputModifier
import data.transport.getTimeNow
import common.util.uuid

@Composable
fun MainBody(
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

    RoomCreationDialog(viewModel)
    AppScaffold(
        scaffoldState = scaffoldState,
        viewModel = viewModel,
        onChatClicked = { id ->
            viewModel.setCurrentConversation(id)
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
    val selectedRoom by viewModel.conversationUiState.collectAsState()
    val user by viewModel.user.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Messages(selectedRoom, user, scrollState)
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            UserInput(
                onMessageSent = { content ->
                    val timeNow = getTimeNow()
                    val message = Message(
                        id = uuid(),
                        roomId = selectedRoom.id,
                        author = viewModel.user.value,
                        content = content,
                        timestamp = timeNow
                    )
                    viewModel.sendMessage(message)
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
            channelName = selectedRoom.channelName,
            channelMembers = selectedRoom.channelMembers,
            onNavIconPressed = onNavIconPressed,
            scrollBehavior = scrollBehavior,
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPaddingMpp(),
        )
    }

}