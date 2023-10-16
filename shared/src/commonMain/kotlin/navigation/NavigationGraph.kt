package navigation

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter

@Parcelize
sealed class Screen: Parcelable {
    data object Login : Screen()

    data object Signup : Screen()

    data class ChatRoom(val id: String) : Screen()

    data class Profile(val email: String) : Screen()
}

@Composable
fun RoutedApp() {
    val router = rememberRouter(Screen::class) { listOf(Screen.Login) }
    RoutedContent(router = router) { screen ->
        when (screen) {
            Screen.Login -> {}
            Screen.Signup -> {}
            is Screen.ChatRoom -> {}
            is Screen.Profile -> {}
        }
    }
}