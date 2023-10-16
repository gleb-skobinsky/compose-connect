package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen

@Composable
fun NavigatedApp() {
    val controller = rememberNavController()
    NavHost(controller, "login") {
        composable("login") {
            LoginScreen { controller.navigate(it.toRoute()) }
        }
        composable("signup") {
            SignupScreen()
        }
        composable("chat/{chatId}") {

        }
        composable("profile/{profileId}") {

        }
        composable("main") {

        }
    }
}