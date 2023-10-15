package presentation.conversation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import domain.model.User
import presentation.SharedViewModelImpl
import presentation.common.themes.ApplicationTheme
import presentation.conversation.ConversationViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.LoginViewModel
import presentation.login_screen.components.AuthScreen

private const val DURATION_MILLIS = 1000

@Composable
fun ChatApplication() {
    val shared = remember { SharedViewModelImpl() }
    val login = remember { LoginViewModel(shared) }
    val drawer = remember { DrawerViewModel(shared) }
    val conversation = remember { ConversationViewModel(shared) }
    ThemeWrapper(login, drawer, conversation)
}


@Composable
fun ThemeWrapper(
    loginViewModel: LoginViewModel,
    drawerViewModel: DrawerViewModel,
    conversationViewModel: ConversationViewModel
) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        val theme by loginViewModel.theme.collectAsState()
        val user by loginViewModel.user.collectAsState()
        ApplicationTheme(theme) {
            Column {
                AnimatedContent(
                    targetState = user != User.Empty,
                    transitionSpec = {
                        if (targetState != initialState && targetState) {
                            slideInVertically(tween(DURATION_MILLIS)) { it } togetherWith slideOutVertically(tween(
                                DURATION_MILLIS
                            )) { -it }
                        } else {
                            slideInVertically(tween(DURATION_MILLIS)) { -it } togetherWith slideOutVertically(tween(
                                DURATION_MILLIS
                            )) { it }
                        }.using(
                            SizeTransform(clip = true)
                        )
                    }
                ) { loggedIn ->
                    if (loggedIn) MainBody(drawerViewModel, conversationViewModel) else AuthScreen(loginViewModel)
                }
            }
        }
    }
}