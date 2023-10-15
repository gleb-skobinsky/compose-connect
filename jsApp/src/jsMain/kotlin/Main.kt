import di.startKoinApp
import presentation.conversation.components.ThemeWrapper
import org.jetbrains.skiko.wasm.onWasmReady


fun main() {
    startKoinApp()
    onWasmReady {
        BrowserViewportWindow("ComposeConnect") {
            ThemeWrapper()
        }
    }
}

