package di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import presentation.SharedAppDataImpl
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel

object NonAndroidViewModelProvider: KoinComponent {
    val sharedVM: SharedAppDataImpl = get()
    val loginVM: LoginViewModel = get()
    val conversationVM: ConversationViewModel = get()
    val drawerVM: DrawerViewModel = get()

    inline operator fun <reified T> invoke(): T = when(T::class) {
        SharedAppDataImpl::class -> sharedVM as T
        LoginViewModel::class -> loginVM as T
        ConversationViewModel::class -> conversationVM as T
        DrawerViewModel::class -> drawerVM as T
        else -> throw IllegalArgumentException("View model of type ${T::class.simpleName} not available.")
    }
}

inline fun <reified T> getKoinInstance(): T {
    return NonAndroidViewModelProvider()
}