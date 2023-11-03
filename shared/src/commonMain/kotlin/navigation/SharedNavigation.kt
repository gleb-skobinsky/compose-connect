package navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.provideViewModel
import presentation.SharedAppDataImpl
import presentation.common.themes.ChirrioAppTheme
import presentation.conversation.ConversationViewModel
import presentation.conversation.components.ChirrioScaffold
import presentation.conversation.components.ConversationContent
import presentation.conversation.components.EmptyStartScreen
import presentation.drawer.DrawerViewModel
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen
import presentation.profile.ProfileViewModel
import presentation.profile.components.ProfileScreen

const val NAVIGATION_TIMEOUT = 700

@Composable
fun SharedNavigatedApp() {
    ChirrioAppTheme {
        val navigator = rememberSharedNavigator()
        CompositionLocalProvider(LocalNavigator provides navigator) {
            AnimatedContent(
                targetState = LocalNavigator.current?.screens,
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                transitionSpec = {
                    fadeIn(tween(NAVIGATION_TIMEOUT)) togetherWith fadeOut(
                        tween(
                            NAVIGATION_TIMEOUT
                        )
                    )
                }
            ) { currentScreen ->
                when (currentScreen) {
                    is Screens.Login -> {
                        ChirrioScaffold(false) {
                            LoginScreen()
                        }
                    }

                    is Screens.Signup -> {
                        ChirrioScaffold(false) {
                            SignupScreen()
                        }
                    }

                    is Screens.Chat -> {
                        ChirrioScaffold {
                            val shared: SharedAppDataImpl = provideViewModel()
                            val drawer: DrawerViewModel = provideViewModel()
                            drawer.setChatId(currentScreen.id)
                            val chatViewModel = remember {
                                ConversationViewModel(
                                    shared,
                                    currentScreen.id
                                )
                            }
                            ConversationContent(chatViewModel)
                        }
                    }

                    is Screens.Profile -> {
                        ChirrioScaffold {
                            val drawer: DrawerViewModel = provideViewModel()
                            drawer.setUserId(currentScreen.id)
                            val shared: SharedAppDataImpl = provideViewModel()
                            val viewModel =
                                remember { ProfileViewModel(shared, currentScreen.id) }
                            ProfileScreen(viewModel)
                        }
                    }

                    is Screens.Main -> {
                        ChirrioScaffold {
                            EmptyStartScreen()
                        }
                    }

                    null -> Unit
                }
            }
        }
    }
}