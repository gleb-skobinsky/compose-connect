package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.SharedViewModelImpl
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel

actual fun startKoinApp() = startKoin {
    modules(
        module {
            viewModel { SharedViewModelImpl() }
            viewModel { DrawerViewModel(get()) }
            viewModel { ConversationViewModel(get()) }
            viewModel { LoginViewModel(get()) }
        }
    )
}