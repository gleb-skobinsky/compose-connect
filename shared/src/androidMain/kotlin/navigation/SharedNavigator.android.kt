package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

actual typealias SharedNavigator = NavHostController

actual fun SharedNavigator.navigateTo(route: Screens) = navigate(route.toRoute())

@Composable
actual fun rememberSharedNavigator(): SharedNavigator = rememberNavController()

actual val SharedNavigator.screens: Screens
    get() = Screens.Login()