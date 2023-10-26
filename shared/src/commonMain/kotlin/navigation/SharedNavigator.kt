package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

expect class SharedNavigator

expect val SharedNavigator.screens: Screens

expect fun SharedNavigator.navigateTo(route: Screens)

@Composable
expect fun rememberSharedNavigator(): SharedNavigator

val LocalNavigator = compositionLocalOf<SharedNavigator?> {
    null
}

class CommonNavigationController() {
    var screen: Screens by mutableStateOf(Screens.Login())
        private set
    fun navigateToRoute(route: Screens) {
        screen = route
    }
}

@Composable
fun rememberCommonNavController() = remember { CommonNavigationController() }