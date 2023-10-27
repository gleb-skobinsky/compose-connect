package navigation

import androidx.compose.runtime.Composable

actual typealias SharedNavigator = CommonNavigationController

actual fun SharedNavigator.navigateTo(route: Screens) = navigateToRoute(route)

@Composable
actual fun rememberSharedNavigator(): SharedNavigator = rememberCommonNavController()

actual val SharedNavigator.screens: Screens
    get() = screen