package presentation.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import di.provideViewModel
import presentation.conversation.ConversationViewModel

@Composable
fun ConversationContent(
    viewModel: ConversationViewModel = provideViewModel(),
) {
    val selectedRoom by viewModel.currentConversation.collectAsState()
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ChatRoom(selectedRoom, viewModel)
        DetailedImageOverlay(viewModel)
    }
}

