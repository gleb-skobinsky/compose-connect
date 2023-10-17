package navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import di.provideViewModel
import presentation.SharedAppDataImpl
import presentation.common.themes.ChirrioAppTheme
import presentation.conversation.ConversationViewModel
import presentation.conversation.components.ChirrioScaffold
import presentation.conversation.components.ConversationContent
import presentation.conversation.components.EmptyStartScreen
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen
import presentation.profile.components.ProfileScreen

const val NAVIGATION_TIMEOUT = 700

@Composable
fun SharedNavigatedApp() {
    ChirrioAppTheme {
        var screen: Screens by remember { mutableStateOf(Screens.Login()) }
        AnimatedContent(
            targetState = screen,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            transitionSpec = {
                fadeIn(tween(NAVIGATION_TIMEOUT)) togetherWith fadeOut(tween(NAVIGATION_TIMEOUT))
            }
        ) { currentScreen ->
            when (currentScreen) {
                is Screens.Login -> {
                    LoginScreen(LoginViewModel(provideViewModel())) { screen = it }
                }

                is Screens.Signup -> {
                    SignupScreen(LoginViewModel(provideViewModel())) { screen = it }
                }

                is Screens.Chat -> {
                    val shared: SharedAppDataImpl = provideViewModel()
                    val drawer: DrawerViewModel = provideViewModel()
                    drawer.setChatId(currentScreen.id)
                    val chatViewModel = remember(currentScreen.id) {
                        ConversationViewModel(
                            shared,
                            currentScreen.id
                        )
                    }
                    ChirrioScaffold(
                        viewModel = drawer,
                        onNavigate = { screen = it }
                    ) {
                        ConversationContent(chatViewModel)
                    }
                }

                is Screens.Profile -> {
                    val viewModel: DrawerViewModel = provideViewModel()
                    viewModel.setUserId(currentScreen.id)
                    ChirrioScaffold(
                        viewModel = viewModel,
                        onNavigate = { screen = it }
                    ) {
                        ProfileScreen(viewModel)
                    }
                }

                is Screens.Main -> {
                    ChirrioScaffold(
                        onNavigate = { screen = it }
                    ) {
                        EmptyStartScreen()
                    }
                }
            }
        }
    }
}