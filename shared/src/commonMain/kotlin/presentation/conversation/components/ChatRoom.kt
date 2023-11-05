package presentation.conversation.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import data.remote.dto.MessageDto
import domain.model.ConversationUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import presentation.common.platform.statusBarsPaddingMpp
import presentation.common.platform.userInputModifier
import presentation.conversation.ConversationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ChatRoom(
    chat: ConversationUiState?,
    viewModel: ConversationViewModel
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
            scrollState = scrollState, modifier = Modifier.weight(1f).fillMaxSize()
        )
        UserInput(
            viewModel = viewModel,
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