package navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import di.provideViewModel
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

@Composable
fun NavigatedApp() {
    ChirrioAppTheme {
        val controller = rememberNavController()
        ChirrioScaffold {
            NavHost(
                navController = controller,
                startDestination = "login",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                enterTransition = { fadeIn(tween(NAVIGATION_TIMEOUT)) },
                exitTransition = { fadeOut(tween(NAVIGATION_TIMEOUT)) },
            ) {
                composable("login") {
                    LoginScreen()
                }
                composable("signup") {
                    SignupScreen()
                }
                composable("chat/{chatId}") {
                    val chatId = it.arguments?.getString("chatId") ?: ""
                    val drawer: DrawerViewModel = provideViewModel()
                    drawer.setChatId(chatId)
                    val viewModel: ConversationViewModel = viewModel(
                        viewModelStoreOwner = it,
                        factory = ConversationVMFactory(chatId, provideViewModel())
                    )
                    ConversationContent(viewModel)
                }
                composable("profile/{profileId}") {
                    val profileId = it.arguments?.getString("profileId") ?: ""
                    val drawer: DrawerViewModel = provideViewModel()
                    drawer.setUserId(profileId)
                    val viewModel: ProfileViewModel = viewModel(
                        viewModelStoreOwner = it,
                        factory = ProfileVMFactory(profileId, provideViewModel())
                    )
                    ProfileScreen(viewModel)
                }
                composable("main") {
                    EmptyStartScreen()
                }
            }
        }
    }
}