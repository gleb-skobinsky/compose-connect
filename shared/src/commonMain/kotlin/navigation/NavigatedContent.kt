package navigation

import androidx.compose.runtime.Composable
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen

@Composable
fun NewNavigation() {
    val controller = rememberMultiplatformNavigator()
    NavigationHost(controller, "login") {
        navComposable("login") {
            LoginScreen()
        }
        navComposable("signup") {
            SignupScreen()
        }
        navComposable("chatroom") {

        }
    }
}