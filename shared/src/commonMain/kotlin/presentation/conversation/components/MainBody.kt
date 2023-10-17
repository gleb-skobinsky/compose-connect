package presentation.conversation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import common.util.uuid
import data.transport.getTimeNow
import di.provideViewModel
import domain.model.AppScreenState
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
import presentation.profile.components.ProfileScreen

@Composable
fun MainBody(
    drawerViewModel: DrawerViewModel = provideViewModel()
) {
    val screenState by drawerViewModel.screenState.collectAsState()

    ChirrioScaffold {
        when (screenState) {
            AppScreenState.CHAT -> ConversationContent()
            AppScreenState.ACCOUNT -> ProfileScreen()
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationContent(
    viewModel: ConversationViewModel = provideViewModel(),
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
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
            scrollBehavior = scrollBehavior,
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPaddingMpp(),
        )
    }
}