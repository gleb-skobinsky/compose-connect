import androidx.compose.runtime.Composable

@Composable
fun App() {
    MainView()
}

expect fun getPlatformName(): String