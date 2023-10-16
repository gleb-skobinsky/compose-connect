package navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable

expect class ExpectedNavigator {

}

expect class ExpectedBackStackEntry {

}

expect fun ExpectedNavigator.getBackStackEntry(route: String): ExpectedBackStackEntry

expect class ExpectedNavigationBuilder {

}

expect fun ExpectedNavigationBuilder.navComposable(
    path: String,
    content: @Composable AnimatedContentScope.(ExpectedBackStackEntry) -> Unit
)

@Composable
expect fun NavigationHost(
    navigator: ExpectedNavigator,
    start: String,
    block: ExpectedNavigationBuilder.() -> Unit
)

@Composable
expect fun rememberMultiplatformNavigator(): ExpectedNavigator