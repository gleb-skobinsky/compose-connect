import androidx.compose.runtime.Composable

@Composable
fun App() {
    Application()
}

expect fun getPlatformName(): String