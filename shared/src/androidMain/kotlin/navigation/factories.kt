package navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import presentation.SharedAppDataImpl
import presentation.conversation.ConversationViewModel

@Suppress("UNCHECKED_CAST")
class ConversationVMFactory(
    private val chatId: String,
    private val sharedData: SharedAppDataImpl
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ConversationViewModel(sharedData, chatId) as T
}