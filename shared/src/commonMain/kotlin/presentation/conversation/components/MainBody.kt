package presentation.conversation.components

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
import common.util.uuid
import domain.model.ConversationUiState
import data.transport.getTimeNow
import domain.model.AppScreenState
import domain.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.common.platform.statusBarsPaddingMpp
import presentation.common.platform.userInputModifier
import presentation.profile.components.ProfileScreen
import presentation.drawer.components.AppScaffold
import presentation.drawer.components.RoomCreationDialog

@Composable
fun MainBody(
    drawerViewModel: DrawerViewModel,
    conversationViewModel: ConversationViewModel
) {
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val drawerOpen by drawerViewModel.drawerShouldBeOpened.collectAsState()
    val screenState by drawerViewModel.screenState.collectAsState()
    val currentUser by drawerViewModel.selectedUserProfile.collectAsState()
    if (drawerOpen) {
        // Open drawer and reset state in VM.
        LaunchedEffect(Unit) {
            scaffoldState.drawerState.open()
            drawerViewModel.resetOpenDrawerAction()
        }
    }

    RoomCreationDialog(drawerViewModel)
    AppScaffold(
        scaffoldState = scaffoldState,
        viewModel = drawerViewModel,
        onChatClicked = { id ->
            drawerViewModel.setCurrentConversation(id)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        onProfileClicked = { userId ->
            drawerViewModel.setCurrentAccount(userId)
            drawerViewModel.setCurrentConversation(ConversationUiState.Empty)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    ) {
        when (screenState) {
            AppScreenState.CHAT -> ConversationContent(
                viewModel = conversationViewModel,
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
    viewModel: ConversationViewModel,
    scrollState: LazyListState,
    scope: CoroutineScope,
    onNavIconPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedRoom by viewModel.currentConversation.collectAsState()
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