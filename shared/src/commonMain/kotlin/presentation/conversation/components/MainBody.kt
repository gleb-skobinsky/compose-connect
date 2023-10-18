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
import common.util.toLocal
import data.remote.dto.MessageDto
import di.provideViewModel
import domain.model.ConversationUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
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
    val currentDate = Clock.System.now().toLocal().date
    val selectedRoom by viewModel.currentConversation.collectAsState()
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ChatRoom(selectedRoom, viewModel, currentDate)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyStartScreen() {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ChirrioAppBar(title = {})
        Text(
            text = "Select a chat to start messaging",
            modifier = Modifier.align(Alignment.Center).border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape).padding(8.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ChatRoom(
    chat: ConversationUiState?,
    viewModel: ConversationViewModel,
    currentDate: LocalDate,
) {
    val user by viewModel.user.collectAsState()
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Column(
        Modifier
            .fillMaxSize()
            .align(Alignment.BottomCenter)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Messages(
            conversationUiState = chat,
            user = user,
            scrollState = scrollState, modifier = Modifier.weight(1f).fillMaxSize(),
            currentDate = currentDate
        )
        UserInput(
            onMessageSent = { content ->
                viewModel.user.value?.let { currentUser ->
                    val message = MessageDto(
                        chatRoom = chat?.id ?: "",
                        user = currentUser,
                        text = content,
                        timestamp = Clock.System.now()
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
            modifier = Modifier.userInputModifier().weight(1f),
        )
    }
    ChannelNameBar(
        channelName = chat?.channelName ?: "",
        channelMembers = chat?.channelMembers ?: 0,
        scrollBehavior = scrollBehavior,
        // Use statusBarsPadding() to move the app bar content below the status bar
        modifier = Modifier.statusBarsPaddingMpp(),
    )
}