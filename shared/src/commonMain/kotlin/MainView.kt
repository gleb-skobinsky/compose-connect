import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import composables.Conversation
import data.MainViewModel
import themes.ApplicationTheme

@Composable
@Suppress("FunctionName")
fun Application() {
    val viewModel = remember { MainViewModel() }
    ThemeWrapper(viewModel)
}

@Composable
@Suppress("FunctionName")
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