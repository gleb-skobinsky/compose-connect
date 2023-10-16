package navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

actual typealias ExpectedNavigator = NavHostController

actual typealias ExpectedBackStackEntry = NavBackStackEntry

actual fun ExpectedNavigator.getBackStackEntry(route: String): ExpectedBackStackEntry = getBackStackEntry(route)


actual typealias ExpectedNavigationBuilder = NavGraphBuilder

actual fun ExpectedNavigationBuilder.navComposable(
    path: String,
    content: @Composable AnimatedContentScope.(ExpectedBackStackEntry) -> Unit
) = composable(route = path, content = content)

@Composable
actual fun NavigationHost(
    navigator: ExpectedNavigator,
    start: String,
    block: ExpectedNavigationBuilder.() -> Unit
) = NavHost(navController = navigator, startDestination = start, builder = block)

@Composable
actual fun rememberMultiplatformNavigator(): ExpectedNavigator = rememberNavController()