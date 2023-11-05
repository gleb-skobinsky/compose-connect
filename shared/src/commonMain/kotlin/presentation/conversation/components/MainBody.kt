package presentation.conversation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import di.provideViewModel
import navigation.LocalNavigator
import navigation.Screens
import navigation.navigateTo
import presentation.drawer.DrawerViewModel
import presentation.drawer.components.AppScaffold
import presentation.drawer.components.RoomCreationDialog

val LocalScaffold = compositionLocalOf {
    ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
}

@Composable
fun ChirrioScaffold(
    hasDrawer: Boolean = true,
    viewModel: DrawerViewModel = provideViewModel(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = LocalScaffold.current
    val navHost = LocalNavigator.current
    CompositionLocalProvider(LocalScaffold provides scaffoldState) {
        RoomCreationDialog(viewModel)
        AppScaffold(
            scaffoldState = scaffoldState,
            onChatClicked = { id ->
                navHost?.navigateTo(Screens.Chat(id = id))
            },
            onProfileClicked = { userId ->
                navHost?.navigateTo(Screens.Profile(id = userId))
            },
            onLogoutClicked = {
                navHost?.navigateTo(Screens.Login())
            },
            hasDrawer = hasDrawer,
            content = content
        )
    }
}