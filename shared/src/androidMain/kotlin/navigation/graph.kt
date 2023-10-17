package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import presentation.conversation.components.ChirrioScaffold
import presentation.conversation.components.ConversationContent
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen
import presentation.profile.components.ProfileScreen

@Composable
fun NavigatedApp() {
    val controller = rememberNavController()
    ChirrioScaffold(onNavigate = {
        controller.navigate(it.toRoute())
    }) {
        NavHost(controller, "login") {
            composable("login") {
                LoginScreen { controller.navigate(it.toRoute()) }
            }
            composable("signup") {
                SignupScreen { controller.navigate(it.toRoute()) }
            }
            composable("chat/{chatId}") {
                ConversationContent()
            }
            composable("profile/{profileId}") {
                ProfileScreen()
            }
            composable("main") {
                ConversationContent()
            }
        }
    }
}