package di

import common.viewmodel.ViewModelPlatformImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import presentation.SharedViewModelImpl
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel

object NonAndroidViewModelProvider: KoinComponent {
    val sharedVM: SharedViewModelImpl = get()
    val loginVM: LoginViewModel = get()
    val conversationVM: ConversationViewModel = get()
    val drawerVM: DrawerViewModel = get()

    inline operator fun <reified T: ViewModelPlatformImpl> invoke(): T = when(T::class) {
        SharedViewModelImpl::class -> sharedVM as T
        LoginViewModel::class -> loginVM as T
        ConversationViewModel::class -> conversationVM as T
        DrawerViewModel::class -> drawerVM as T
        else -> throw IllegalArgumentException("View model of type ${T::class.simpleName} not available.")
    }
}

inline fun <reified T: ViewModelPlatformImpl> getKoinInstance(): T {

    return NonAndroidViewModelProvider()
}