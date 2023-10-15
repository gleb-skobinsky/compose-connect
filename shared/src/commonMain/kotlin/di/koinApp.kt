package di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.SharedAppDataImpl
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel

expect fun startKoinApp(): KoinApplication

fun startCommonKoinApp() = startKoin {
    modules(
        module {
            single { SharedAppDataImpl() }
            single { DrawerViewModel(get()) }
            single { LoginViewModel(get()) }
            single { ConversationViewModel(get()) }
        }
    )
}