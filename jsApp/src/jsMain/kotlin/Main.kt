import di.startKoinApp
import org.jetbrains.skiko.wasm.onWasmReady
import presentation.conversation.components.ThemeWrapper


fun main() {
    startKoinApp()
    onWasmReady {
        BrowserViewportWindow("ComposeConnect") {
            ThemeWrapper()
        }
    }
}

