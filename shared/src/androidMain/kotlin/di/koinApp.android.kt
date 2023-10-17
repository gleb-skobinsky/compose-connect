package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.SharedAppDataImpl
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel

actual fun startKoinApp() = startKoin {
    modules(
        module {
            single { SharedAppDataImpl() }
            single { DrawerViewModel(get()) }
            viewModel { ConversationViewModel(get()) }
            viewModel { LoginViewModel(get()) }
        }
    )
}