import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import composables.Conversation
import composables.LoginScreen
import data.MainViewModel
import themes.ApplicationTheme

@Composable
fun ChatApplication() {
    val viewModel = remember { MainViewModel() }
    ThemeWrapper(viewModel)
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThemeWrapper(
    viewModel: MainViewModel,
) {
    val theme by viewModel.themeMode.collectAsState()
    val user by viewModel.user.collectAsState()
    ApplicationTheme(theme) {
        Column {
            AnimatedContent(
                targetState = user != null,
                transitionSpec = {
                    val durationMillis = 1000
                    if (targetState != initialState && targetState) {
                        (slideInVertically(animationSpec = tween(durationMillis)) { height -> height })
                            .with(slideOutVertically(animationSpec = tween(durationMillis)) { height -> -height })
                    } else {
                        slideInVertically(animationSpec = tween(durationMillis)) { height -> -height } with
                                slideOutVertically(animationSpec = tween(durationMillis)) { height -> height }
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) {
                if (it) Conversation(viewModel) else LoginScreen(viewModel)
            }
        }
    }
}