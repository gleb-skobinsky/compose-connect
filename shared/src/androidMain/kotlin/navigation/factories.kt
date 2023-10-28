package navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import presentation.SharedAppDataImpl
import presentation.conversation.ConversationViewModel
import presentation.profile.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ConversationVMFactory(
    private val chatId: String,
    private val sharedData: SharedAppDataImpl
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ConversationViewModel(sharedData, chatId) as T
}

@Suppress("UNCHECKED_CAST")
class ProfileVMFactory(
    private val profileId: String,
    private val sharedData: SharedAppDataImpl
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfileViewModel(sharedData, profileId) as T
}