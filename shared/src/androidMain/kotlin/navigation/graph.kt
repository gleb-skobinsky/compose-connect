package navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import di.provideViewModel
import presentation.common.themes.ChirrioAppTheme
import presentation.conversation.ConversationViewModel
import presentation.conversation.components.ChirrioScaffold
import presentation.conversation.components.ConversationContent
import presentation.drawer.DrawerViewModel
import presentation.login_screen.components.LoginScreen
import presentation.login_screen.components.SignupScreen
import presentation.profile.components.ProfileScreen

@Composable
fun NavigatedApp() {
    ChirrioAppTheme {
        val controller = rememberNavController()
        NavHost(
            navController = controller,
            startDestination = "login",
            enterTransition = {
                slideInHorizontally(tween(NAVIGATION_TIMEOUT)) { it }
            },
            exitTransition = {
                slideOutHorizontally(tween(NAVIGATION_TIMEOUT)) { -it }
            },
            popEnterTransition = {
                slideInHorizontally(tween(NAVIGATION_TIMEOUT)) { -it }
            },
            popExitTransition = {
                slideOutHorizontally(tween(NAVIGATION_TIMEOUT)) { it }
            }
        ) {
            composable("login") {
                LoginScreen { controller.navigate(it.toRoute()) }
            }
            composable("signup") {
                SignupScreen { controller.navigate(it.toRoute()) }
            }
            composable("chat/{chatId}") {
                val chatId = it.arguments?.getString("chatId") ?: ""
                val drawer: DrawerViewModel = provideViewModel()
                drawer.setChatId(chatId)
                val viewModel: ConversationViewModel = viewModel(
                    viewModelStoreOwner = it,
                    factory = ConversationVMFactory(chatId, provideViewModel())
                )
                ChirrioScaffold(
                    viewModel = drawer,
                    onNavigate = { screen ->
                        controller.navigate(screen.toRoute())
                    }
                ) {
                    ConversationContent(viewModel)
                }
            }
            composable("profile/{profileId}") {
                val profileId = it.arguments?.getString("profileId") ?: ""
                val drawer: DrawerViewModel = provideViewModel()
                drawer.setUserId(profileId)
                ChirrioScaffold(
                    viewModel = drawer,
                    onNavigate = { screen ->
                        controller.navigate(screen.toRoute())
                    }
                ) {
                    ProfileScreen(drawer)
                }
            }
            composable("main") {
                ChirrioScaffold(onNavigate = { screen ->
                    controller.navigate(screen.toRoute())
                }) {
                    ConversationContent()
                }
            }
        }
    }
}