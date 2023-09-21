import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import composables.AuthScreen
import composables.Conversation
import data.MainViewModel
import data.User
import themes.ApplicationTheme

private const val durationMillis = 1000

@Composable
fun ChatApplication() {
    val viewModel = remember { MainViewModel() }
    ThemeWrapper(viewModel)
}


@Composable
fun ThemeWrapper(
    viewModel: MainViewModel,
) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        val theme by viewModel.themeMode.collectAsState()
        val user by viewModel.user.collectAsState()
        ApplicationTheme(theme) {
            Column {
                AnimatedContent(
                    targetState = user != User.Empty,
                    transitionSpec = {
                        if (targetState != initialState && targetState) {
                            slideInVertically(tween(durationMillis)) { it } togetherWith slideOutVertically(tween(durationMillis)) { -it }
                        } else {
                            slideInVertically(tween(durationMillis)) { -it } togetherWith slideOutVertically(tween(durationMillis)) { it }
                        }.using(
                            SizeTransform(clip = true)
                        )
                    }
                ) { loggedIn ->
                    if (loggedIn) Conversation(viewModel) else AuthScreen(viewModel)
                }
            }
        }
    }
}