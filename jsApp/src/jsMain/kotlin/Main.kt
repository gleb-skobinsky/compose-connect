import di.startKoinApp
import navigation.SharedNavigatedApp
import org.jetbrains.skiko.wasm.onWasmReady


fun main() {
    startKoinApp()
    onWasmReady {
        BrowserViewportWindow("Chirrio Messenger") {
            SharedNavigatedApp()
        }
    }
}

