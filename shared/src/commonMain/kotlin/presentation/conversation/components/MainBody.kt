package presentation.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import common.util.uuid
import data.transport.getTimeNow
import di.provideViewModel
import domain.model.ConversationUiState
import domain.model.Message
import kotlinx.coroutines.launch
import navigation.NavigationCallback
import navigation.Screens
import presentation.common.platform.statusBarsPaddingMpp
import presentation.common.platform.userInputModifier
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.drawer.components.AppScaffold
import presentation.drawer.components.RoomCreationDialog

val LocalScaffold = compositionLocalOf {
    ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
}

@Composable
fun ChirrioScaffold(
    viewModel: DrawerViewModel = provideViewModel(),
    onNavigate: NavigationCallback = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = LocalScaffold.current
    val drawerOpen by viewModel.drawerShouldBeOpened.collectAsState()
    if (drawerOpen) {
        LaunchedEffect(Unit) {
            scaffoldState.drawerState.open()
            viewModel.resetOpenDrawerAction()
        }
    }
    CompositionLocalProvider(LocalScaffold provides scaffoldState) {
        RoomCreationDialog(viewModel)
        AppScaffold(
            scaffoldState = scaffoldState,
            onChatClicked = { id ->
                onNavigate(Screens.Chat(id = id))
            },
            onProfileClicked = { userId ->
                onNavigate(Screens.Profile(id = userId))
            },
            onLogoutClicked = {
                onNavigate(Screens.Login())
            },
            content = content
        )
    }
}

@Composable
fun ConversationContent(
    viewModel: ConversationViewModel = provideViewModel(),
) {
    val selectedRoom by viewModel.currentConversation.collectAsState()
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        selectedRoom?.let { chat ->
            ChatRoom(chat, viewModel)
        } ?: run {
            EmptyStartScreen()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.EmptyStartScreen() {
    ChirrioAppBar(title = {})
    Text(
        text = "Select a chat to start messaging",
        modifier = Modifier.align(Alignment.Center).border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape).padding(8.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ChatRoom(
    chat: ConversationUiState,
    viewModel: ConversationViewModel,
) {
    val user by viewModel.user.collectAsState()
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Messages(chat, user, scrollState)
    Column(
        Modifier.Companion
            .align(Alignment.BottomCenter)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        UserInput(
            onMessageSent = { content ->
                viewModel.user.value?.let { currentUser ->
                    val timeNow = getTimeNow()
                    val message = Message(
                        id = uuid(),
                        roomId = chat.id,
                        author = currentUser,
                        content = content,
                        timestamp = timeNow
                    )
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
        channelName = chat.channelName,
        channelMembers = chat.channelMembers,
        scrollBehavior = scrollBehavior,
        // Use statusBarsPadding() to move the app bar content below the status bar
        modifier = Modifier.statusBarsPaddingMpp(),
    )
}