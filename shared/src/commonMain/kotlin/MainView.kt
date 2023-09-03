import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import composables.Conversation
import data.MainViewModel
import themes.ApplicationTheme

@Composable
fun Application() {
    val viewModel = remember { MainViewModel() }
    ThemeWrapper(viewModel)
}

@Composable
fun ThemeWrapper(
    viewModel: MainViewModel
) {
    val theme by viewModel.themeMode.collectAsState()
    ApplicationTheme(theme) {
        Column {
            Conversation(
                viewModel = viewModel
            )
        }
    }
}