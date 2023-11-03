import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import di.startKoinApp
import navigation.SharedNavigatedApp
import org.jetbrains.skiko.wasm.onWasmReady


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoinApp()
    onWasmReady {
        CanvasBasedWindow("Chirrio Messenger") {
            SharedNavigatedApp()
        }
    }
}

