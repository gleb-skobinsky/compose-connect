package navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
        CompositionLocalProvider(LocalNavigator provides controller) {
            NavHost(
                navController = controller,
                startDestination = "login",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                enterTransition = { fadeIn(tween(NAVIGATION_TIMEOUT)) },
                exitTransition = { fadeOut(tween(NAVIGATION_TIMEOUT)) },
            ) {
                composable("login") {
                    ChirrioScaffold(false) {
                        LoginScreen()
                    }
                }
                composable("signup") {
                    ChirrioScaffold(false) {
                        SignupScreen()
                    }
                }
                composable("chat/{chatId}") { entry ->
                    ChirrioScaffold {
                        val chatId = entry.arguments?.getString("chatId") ?: ""
                        val drawer: DrawerViewModel = provideViewModel()
                        drawer.setChatId(chatId)
                        val viewModel: ConversationViewModel = viewModel(
                            viewModelStoreOwner = entry,
                            factory = ConversationVMFactory(chatId, provideViewModel())
                        )
                        ConversationContent(viewModel)
                    }
                }
                composable("profile/{profileId}") { entry ->
                    ChirrioScaffold {
                        val profileId = entry.arguments?.getString("profileId") ?: ""
                        val drawer: DrawerViewModel = provideViewModel()
                        drawer.setUserId(profileId)
                        val viewModel: ProfileViewModel = viewModel(
                            viewModelStoreOwner = entry,
                            factory = ProfileVMFactory(profileId, provideViewModel())
                        )
                        ProfileScreen(viewModel)
                    }
                }
                composable("main") {
                    ChirrioScaffold {
                        EmptyStartScreen()
                    }
                }
            }
        }
    }
}